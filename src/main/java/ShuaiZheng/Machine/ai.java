package ShuaiZheng.Machine; /**
 * Created by Shuai Zheng on 2/12/17.
 */

import org.apache.commons.io.FileUtils;
import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.FlipImageTransform;
import org.datavec.image.transform.ImageTransform;
import org.datavec.image.transform.WarpImageTransform;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Animal Classification
 *
 * Example classification of photos from 4 different animals (bear, duck, deer, turtle).
 *
 * References:
 *  - U.S. Fish and Wildlife Service (animal sample dataset): http://digitalmedia.fws.gov/cdm/
 *  - Tiny ImageNet Classification with CNN: http://cs231n.stanford.edu/reports/leonyao_final.pdf
 *
 * CHALLENGE: Current setup gets low score results. Can you improve the scores? Some approaches:
 *  - Add additional images to the dataset
 *  - Apply more transforms to dataset
 *  - Increase epochs
 *  - Try different model configurations
 *  - Tune by adjusting learning rate, updaters, activation & loss functions, regularization, ...
 */

public class ai {
    protected  int height = 100;
    protected  int width = 100;
    protected  int channels = 3;
    protected  int numExamples = 80;
    protected  int numLabels = 4;
    protected  int batchSize = 20;

    private   String INPUT_ZIP_FILE = "/Users/zhengshuai/Desktop/Archive.zip";
    private   String OUTPUT_FOLDER = "/Users/zhengshuai/test";

    protected  long seed = 42;
    protected  Random rng = new Random(seed);
    protected  int listenerFreq = 1;
    protected  int iterations = 1;
    protected  int epochs = 1;
    protected  double splitTrainTest = 0.8;
    protected  int nCores = 2;
    protected  boolean save = false;

    protected  String modelType = "custom"; // LeNet, AlexNet or Custom but you need to fill it out
    private String LibPath;


    public void NetRun(String[] args) throws Exception {
        if (args[0] == "train") {


        }

    }

    public ai(String INPUT_ZIP_FILE, String OUTPUT_FOLDER,String Lib) {
        this.INPUT_ZIP_FILE = INPUT_ZIP_FILE;
        this.OUTPUT_FOLDER = OUTPUT_FOLDER;
        this.LibPath=Lib;
    }

//    public void train(){
//
//        log.info("Load data....");
//
//        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
//        File mainPath = new File(System.getProperty("user.dir"), "src/main/resources/image/");
//        FileSplit fileSplit = new FileSplit(mainPath, NativeImageLoader.ALLOWED_FORMATS, rng);
//        BalancedPathFilter pathFilter = new BalancedPathFilter(rng, labelMaker, numExamples, numLabels, batchSize);
//
//        InputSplit[] inputSplit = fileSplit.sample(pathFilter, numExamples * (1 + splitTrainTest), numExamples * (1 - splitTrainTest));
//
//        InputSplit trainData = inputSplit[0];
//        InputSplit testData = inputSplit[1];
//
//        ImageTransform flipTransform1 = new FlipImageTransform(rng);
//        ImageTransform flipTransform2 = new FlipImageTransform(new Random(123));
//        ImageTransform warpTransform = new WarpImageTransform(rng, 42);
////        ImageTransform colorTransform = new ColorConversionTransform(new Random(seed), COLOR_BGR2YCrCb);
//        List<ImageTransform> transforms = Arrays.asList(new ImageTransform[]{flipTransform1, warpTransform, flipTransform2});
//
//        /**
//         * Data Setup -> normalization
//         *  - how to normalize images and generate large dataset to train on
//         **/
//        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
//
//        log.info("Build model....");
//
//        // Uncomment below to try AlexNet. Note change height and width to at least 100
////        MultiLayerNetwork network = new AlexNet(height, width, channels, numLabels, seed, iterations).init();
//
//        MultiLayerNetwork network = null;
//        VGGNetA a= new VGGNetA(height,width,channels,numLabels,seed,iterations);
//        network= a.init();
//        network.init();
//        network.setListeners(new ScoreIterationListener(listenerFreq));
//
//
//        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, labelMaker);
//        DataSetIterator dataIter;
//        MultipleEpochsIterator trainIter;
//
//
//        log.info("Train model....");
//        // Train without transformations
////        recordReader.initialize(trainData, null);
////        dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, numLabels);
////        scaler.fit(dataIter);
////        dataIter.setPreProcessor(scaler);
////        trainIter = new MultipleEpochsIterator(epochs, dataIter, nCores);
////        network.fit(trainIter);
//
////         Train with transformations
////        for (ImageTransform transform : transforms) {
////            System.out.print("\nTraining on transformation: " + transform.getClass().toString() + "\n\n");
////            recordReader.initialize(trainData, transform);
////            dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, numLabels);
////            scaler.fit(dataIter);
////            dataIter.setPreProcessor(scaler);
////            trainIter = new MultipleEpochsIterator(epochs, dataIter, nCores);
////            network.fit(trainIter);
////        }
//
//        log.info("Evaluate model....");
//        recordReader.initialize(testData);
//        dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, numLabels);
//        scaler.fit(dataIter);
//        dataIter.setPreProcessor(scaler);
////        Evaluation eval = network.evaluate(dataIter);
////        log.info(eval.stats(true));
//
//        // Example on how to get predict results with trained model
//        //Load the model
//        File locationToSave = new File("ShuaiZhengTrained.zip");
//        MultiLayerNetwork restore = ModelSerializer.restoreMultiLayerNetwork(locationToSave);
//
//        dataIter.reset();
//
//        DataSet testDataSet = dataIter.next();
//        String expectedResult = testDataSet.getLabelName(0);
//        System.out.println(expectedResult);
//        List<String> predict = restore.predict(testDataSet);
//        String modelResult = predict.get(0);
//        System.out.print("\nFor a single example that is labeled " + expectedResult + " the model predicted " + modelResult + "\n\n");
//
////
////        File locationToSave = new File("ShuaiZhengTrained2.zip");      //Where to save the network. Note: the file is in .zip format - can be opened externally
////        boolean saveUpdater = true;                                             //Updater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this if you want to train your network more in the future
////        ModelSerializer.writeModel(network, locationToSave, saveUpdater);
//
//
//        if (save) {
//            log.info("Save model....");
//            String basePath = FilenameUtils.concat(System.getProperty("user.dir"), "src/main/resources/");
//            NetSaverLoaderUtils.saveNetworkAndParameters(network, basePath);
//            NetSaverLoaderUtils.saveUpdators(network, basePath);
//        }
//        log.info("****************Example finished********************");
//
//
//    }


    public void run(String[] args) throws Throwable {

        /**cd
         * Data Setup -> organize and limit data file paths:
         *  - mainPath = path to image files
         *  - fileSplit = define basic dataset split with limits on format
         *  - pathFilter = define additional file load filter to limit size and balance batch content
         **/

        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
        File mainPath = new File(OUTPUT_FOLDER + File.separator + "animals" );
        mainPath.mkdir();
                UnzipUtility unzipper = new UnzipUtility();
        try {
            unzipper.unzip(INPUT_ZIP_FILE, OUTPUT_FOLDER + File.separator + "animals");
        } catch (Exception ex) {
// some errors occurred

        }

        FileSplit fileSplit = new FileSplit(mainPath, NativeImageLoader.ALLOWED_FORMATS, rng);
        BalancedPathFilter pathFilter = new BalancedPathFilter(rng, labelMaker, numExamples, numLabels, batchSize);

//        File mainPath = new File(System.getProperty("user.dir"), "dl4j-examples/src/main/resources/animals/");

        InputSplit[] inputSplit = fileSplit.sample(pathFilter, 0, 100);

        InputSplit testData = inputSplit[1];

        /**
         * Data Setup -> transformation
         *  - Transform = how to tranform images and generate large dataset to train on
         **/
        ImageTransform flipTransform1 = new FlipImageTransform(rng);
        ImageTransform flipTransform2 = new FlipImageTransform(new Random(123));
        ImageTransform warpTransform = new WarpImageTransform(rng, 42);
//        ImageTransform colorTransform = new ColorConversionTransform(new Random(seed), COLOR_BGR2YCrCb);
        List<ImageTransform> transforms = Arrays.asList(new ImageTransform[]{flipTransform1, warpTransform, flipTransform2});

        /**
         * Data Setup -> normalization
         *  - how to normalize images and generate large dataset to train on
         **/
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);


        // Uncomment below to try AlexNet. Note change height and width to at least 100
//        MultiLayerNetwork network = new AlexNet(height, width, channels, numLabels, seed, iterations).init();

        MultiLayerNetwork network = null;

        /**
         * Data Setup -> define how to load data into net:
         *  - recordReader = the reader that loads and converts image data pass in inputSplit to initialize
         *  - dataIter = a generator that only loads one batch at a time into memory to save memory
         *  - trainIter = uses MultipleEpochsIterator to ensure model runs through the data for all epochs
         **/
        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, labelMaker);
        DataSetIterator dataIter;






//        File locationToSave = new File("ShuaiZhengTrained.zip");
//        System.out.println(this.getClass().getClassLoader().getResource("ShuaiZhengTrained.zip").getPath());

        File locationToSave =new File(LibPath);
//        locationToSave= new File(this.getClass().getClassLoader().getResource("ShuaiZhengTrained.zip").getPath());
///WEB-INF/lib/testai.jar/ShuaiZhengTrained.zip

        System.out.println(OUTPUT_FOLDER+"/resources/ShuaiZhengTrained.zip");

       MultiLayerNetwork restore = ModelSerializer.restoreMultiLayerNetwork(locationToSave);
//        MultiLayerNetwork restore ;
//
//                VGGNetA a= new VGGNetA(height,width,channels,numLabels,seed,iterations);
//        restore= a.init();

        recordReader.initialize(testData);
        dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, numLabels);
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);
        Evaluation eval = restore.evaluate(dataIter);
//        System.out.println(eval);






//        log.info(eval.stats(true));
        System.out.println(eval.stats(true));
//
        try
        {
            PrintWriter out = new PrintWriter(OUTPUT_FOLDER + File.separator + "output.txt");Throwable localThrowable2 = null;
//            PrintWriter out = new PrintWriter("/Users/zhengshuai/test/output.txt");Throwable localThrowable2 = null;

            try
            {
                out.print(eval.stats(true));
            }
            catch (Throwable localThrowable1)
            {
                localThrowable2 = localThrowable1;throw localThrowable1;
            }
            finally
            {
                if (out != null) {
                    if (localThrowable2 != null) {
                        try
                        {
                            out.close();
                        }
                        catch (Throwable x2)
                        {
                        }
                    } else {
                        out.close();
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

//                dataIter.reset();
//
//        DataSet testDataSet = dataIter.next();
//        String expectedResult = testDataSet.getLabelName(0);
//        System.out.println(expectedResult);
//        List<String> predict = restore.predict(testDataSet);
//        String modelResult = predict.get(0);
//        System.out.print("\nFor a single example that is labeled " + expectedResult + " the model predicted " + modelResult + "\n\n");


//        FileUtils.deleteDirectory(mainPath);



    }



//    private ConvolutionLayer convInit(String name, int in, int out, int[] kernel, int[] stride, int[] pad, double bias) {
//        return new ConvolutionLayer.Builder(kernel, stride, pad).name(name).nIn(in).nOut(out).biasInit(bias).build();
//    }
//
//    private ConvolutionLayer conv3x3(String name, int out, double bias) {
//        return new ConvolutionLayer.Builder(new int[]{3, 3}, new int[]{1, 1}, new int[]{1, 1}).name(name).nOut(out).biasInit(bias).build();
//    }
//
//    private ConvolutionLayer conv5x5(String name, int out, int[] stride, int[] pad, double bias) {
//        return new ConvolutionLayer.Builder(new int[]{5, 5}, stride, pad).name(name).nOut(out).biasInit(bias).build();
//    }
//
//    private SubsamplingLayer maxPool(String name, int[] kernel) {
//        return new SubsamplingLayer.Builder(kernel, new int[]{2, 2}).name(name).build();
//    }
//
//    private DenseLayer fullyConnected(String name, int out, double bias, double dropOut, Distribution dist) {
//        return new DenseLayer.Builder().name(name).nOut(out).biasInit(bias).dropOut(dropOut).dist(dist).build();
//    }

//    public MultiLayerNetwork lenetModel() {
//        /**
//         * Revisde Lenet Model approach developed by ramgo2 achieves slightly above random
//         * Reference: https://gist.github.com/ramgo2/833f12e92359a2da9e5c2fb6333351c5
//         **/
//        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
//                .seed(seed)
//                .iterations(iterations)
//                .regularization(false).l2(0.005) // tried 0.0001, 0.0005
//                .activation("relu")
//                .learningRate(0.0001) // tried 0.00001, 0.00005, 0.000001
//                .weightInit(WeightInit.XAVIER)
//                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
//                .updater(Updater.RMSPROP).momentum(0.9)
//                .list()
//                .layer(0, convInit("cnn1", channels, 50, new int[]{5, 5}, new int[]{1, 1}, new int[]{0, 0}, 0))
//                .layer(1, maxPool("maxpool1", new int[]{2, 2}))
//                .layer(2, conv5x5("cnn2", 100, new int[]{5, 5}, new int[]{1, 1}, 0))
//                .layer(3, maxPool("maxool2", new int[]{2, 2}))
//                .layer(4, new DenseLayer.Builder().nOut(500).build())
//                .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
//                        .nOut(numLabels)
//                        .activation("softmax")
//                        .build())
//                .backprop(true).pretrain(false)
//                .setInputType(InputType.convolutional(height, width, channels))
//                .build();
//
//        return new MultiLayerNetwork(conf);
//
//    }

//    public MultiLayerNetwork alexnetModel() {
//        /**
//         * AlexNet model interpretation based on the original paper ImageNet Classification with Deep Convolutional Neural Networks
//         * and the imagenetExample code referenced.
//         * http://papers.nips.cc/paper/4824-imagenet-classification-with-deep-convolutional-neural-networks.pdf
//         **/
//
//        double nonZeroBias = 1;
//        double dropOut = 0.5;
//
//        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
//                .seed(seed)
//                .weightInit(WeightInit.DISTRIBUTION)
//                .dist(new NormalDistribution(0.0, 0.01))
//                .activation("relu")
//                .updater(Updater.NESTEROVS)
//                .iterations(iterations)
//                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer) // normalize to prevent vanishing or exploding gradients
//                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
//                .learningRate(1e-2)
//                .biasLearningRate(1e-2 * 2)
//                .learningRateDecayPolicy(LearningRatePolicy.Step)
//                .lrPolicyDecayRate(0.1)
//                .lrPolicySteps(100000)
//                .regularization(true)
//                .l2(5 * 1e-4)
//                .momentum(0.9)
//                .miniBatch(false)
//                .list()
//                .layer(0, convInit("cnn1", channels, 96, new int[]{11, 11}, new int[]{4, 4}, new int[]{3, 3}, 0))
//                .layer(1, new LocalResponseNormalization.Builder().name("lrn1").build())
//                .layer(2, maxPool("maxpool1", new int[]{3, 3}))
//                .layer(3, conv5x5("cnn2", 256, new int[]{1, 1}, new int[]{2, 2}, nonZeroBias))
//                .layer(4, new LocalResponseNormalization.Builder().name("lrn2").build())
//                .layer(5, maxPool("maxpool2", new int[]{3, 3}))
//                .layer(6, conv3x3("cnn3", 384, 0))
//                .layer(7, conv3x3("cnn4", 384, nonZeroBias))
//                .layer(8, conv3x3("cnn5", 256, nonZeroBias))
//                .layer(9, maxPool("maxpool3", new int[]{3, 3}))
//                .layer(10, fullyConnected("ffn1", 4096, nonZeroBias, dropOut, new GaussianDistribution(0, 0.005)))
//                .layer(11, fullyConnected("ffn2", 4096, nonZeroBias, dropOut, new GaussianDistribution(0, 0.005)))
//                .layer(12, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
//                        .name("output")
//                        .nOut(numLabels)
//                        .activation("softmax")
//                        .build())
//                .backprop(true)
//                .pretrain(false)
//                .setInputType(InputType.convolutional(height, width, channels))
//                .build();
//
//        return new MultiLayerNetwork(conf);
//
//    }

}

