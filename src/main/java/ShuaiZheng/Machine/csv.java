package ShuaiZheng.Machine;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Shuai Zheng on 2/20/17.
 */
public class csv {



        private  Logger log = LoggerFactory.getLogger(org.deeplearning4j.examples.dataExamples.CSVExample.class);

        public  void csvmain(String[] args) throws Throwable {

            //First: get the dataset using the record reader. CSVRecordReader handles loading/parsing
            int numLinesToSkip = 0;
            String delimiter = ",";
            RecordReader recordReader = new CSVRecordReader(numLinesToSkip, delimiter);
            recordReader.initialize(new FileSplit(new File(args[0])));


            //Second: the RecordReaderDataSetIterator handles conversion to DataSet objects, ready for use in neural network
            int labelIndex = 4;     //5 values in each row of the iris.txt CSV: 4 input features followed by an integer label (class) index. Labels are the 5th value (index 4) in each row
            int numClasses = 3;     //3 classes (types of iris flowers) in the iris data set. Classes have integer values 0, 1 or 2
            int batchSize = 150;    //Iris data set: 150 examples total. We are loading all of them into one DataSet (not recommended for large data sets)

            DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, numClasses);
            DataSet allData = iterator.next();
            allData.shuffle();
            SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.65);  //Use 65% of data for training

            DataSet trainingData = testAndTrain.getTrain();
            DataSet testData = testAndTrain.getTest();

            //We need to normalize our data. We'll use NormalizeStandardize (which gives us mean 0, unit variance):
            DataNormalization normalizer = new NormalizerStandardize();
            normalizer.fit(trainingData);
            //Collect the statistics (mean/stdev) from the training data. This does not modify the input data
            normalizer.transform(trainingData);
            //Apply normalization to the training data
            normalizer.transform(testData);
            //Apply normalization to the test data. This is using statistics calculated from the *training* set


            final int numInputs = 4;
            int outputNum = 3;
            int iterations = 1000;
            long seed = 6;


            log.info("Build model....");
            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                    .seed(seed)
                    .iterations(iterations)
                    .activation("tanh")
                    .weightInit(WeightInit.XAVIER)
                    .learningRate(0.1)
                    .regularization(true).l2(1e-4)
                    .list()
                    .layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(3)
                            .build())
                    .layer(1, new DenseLayer.Builder().nIn(3).nOut(3)
                            .build())

                    .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                            .activation("softmax")
                            .nIn(3).nOut(outputNum).build())
                    .backprop(true).pretrain(false)
                    .build();

            //run the model
            MultiLayerNetwork model = new MultiLayerNetwork(conf);
            model.init();
            model.setListeners(new ScoreIterationListener(100));

            model.fit(trainingData);

            //evaluate the model on the test set
            Evaluation eval = new Evaluation(3);
            INDArray output = model.output(testData.getFeatureMatrix());
            eval.eval(testData.getLabels(), output);
            log.info(eval.stats());


            try {
                PrintWriter out = new PrintWriter(args[1] + File.separator + "output.txt");
                Throwable localThrowable2 = null;
//            PrintWriter out = new PrintWriter("/Users/zhengshuai/test/output.txt");Throwable localThrowable2 = null;

                try {
                    out.print(eval.stats());
                } catch (Throwable localThrowable1) {
                    localThrowable2 = localThrowable1;
                    throw localThrowable1;
                } finally {
                    if (out != null) {
                        if (localThrowable2 != null) {
                            try {
                                out.close();
                            } catch (Throwable x2) {
                            }
                        } else {
                            out.close();
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

}
