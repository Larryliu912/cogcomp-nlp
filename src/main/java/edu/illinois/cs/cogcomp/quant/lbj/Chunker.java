// Modifying this comment will cause the next execution of LBJava to overwrite this file.
// F1B88000000000000000D835D4F4BD0401DB3FB26411784220BD051792905E3A542AA820284B84DBD0BB36B75EDCEEA677D90101DFDED1FA305ADB4B9B8DB397BF6EDB973EC7C3ED10813CC80DB1290243680AA454E126D811C9729A44C00807E757D02AE6D43011B28A4DA8C0757D6DB40F4C8B046229C4C68D1E722CA4A3958D68CC42F4FE910C3F9A65CC073E965AC6B16185587182DBD56AB71B6510C1A860B220432FD0E7D777FAC84BBEE1BC83A48F9024DE9AC9E0AE81DD7CC37D78E33112B655B22329D6EF358DA48D5ABC138D23EA1D356EA67F92434B99E5ECECFB23FE0E48F998C8F0D93BA8DAF652C427C39ECBCA94BEEE9DB3AB4BE7916BE86FA7919EDF20D9A88A5D316456F5D2FED6FFCB7E13D263E8EAC49C7DFF440B1D774376569AE19E5B1CB9F506990A5D3874FB106320D60E8B85459481B5E9E3F0771C3EB4C4FE254182A6A00A832CD669CDA14994C9FE22AC22BAB7B6AA84BCFD2C45485B2E11405B47A73C96373CE90658AB520B56F9C5AB6D96AFBBB59D15C65B91B66FD4BA576DD9255E41BD85B5F03752A18B55F4F465BDD181F83709A280EE64071DDED19F1629A967E64DB339AAF3629F039D70DDD227F8F91E3096E939235DB21CEEEE14F31534B6EA92D2D399B4E2C77211DAF03C457B3F9DCE0B82A8B14F18B9E5853122A9812B8E15916ABFC811743283A20CBD6D84ED0151B68B37D1E7E4195053DF705EF97B5D3B5197ED829734E509B8EDA9C89D179C6EC3FCA06A941DF706783A360795279C24E61978335822B38EAAC12886C0580C5A3C36870C043B5C7BF707F0D133EB080E821E8060E9347BBC100A67532317174D529F5E19DAB88030E44A9F1ACD08B86B2EDAB07DD647D648F0547F30232F71C7610CF325400000

package edu.illinois.cs.cogcomp.quant.lbj;

import edu.illinois.cs.cogcomp.lbj.pos.POSTagger;
import edu.illinois.cs.cogcomp.lbj.pos.POSWindow;
import edu.illinois.cs.cogcomp.lbjava.classify.*;
import edu.illinois.cs.cogcomp.lbjava.infer.*;
import edu.illinois.cs.cogcomp.lbjava.io.IOUtilities;
import edu.illinois.cs.cogcomp.lbjava.learn.*;
import edu.illinois.cs.cogcomp.lbjava.nlp.*;
import edu.illinois.cs.cogcomp.lbjava.nlp.seg.Token;
import edu.illinois.cs.cogcomp.lbjava.parse.*;
import java.util.*;
import java.util.regex.*;


/**
  * Learned classifier that predicts a BIO chunk tag given a word represented
  * as a <code>Token</code>.  {@link PreviousTags} from
  * this package and {@link POSWindow} from the
  * <a href="http://l2r.cs.uiuc.edu/~cogcomp/asoftware.php?skey=FLBJPOS">LBJ
  * POS tagger package</a> as well as <code>Forms</code>,
  * <code>Capitalization</code>, <code>WordTypeInformation</code>, and
  * <code>Affixes</code> from the LBJ library are used as features.  This
  * classifier caches its prediction in the <code>Token.type</code> field, and
  * it will simply return the value of this field as its prediction if it is
  * non-null.
  *
  * @author Nick Rizzolo
 **/


public class Chunker extends SparseNetworkLearner
{
  private static java.net.URL _lcFilePath;
  private static java.net.URL _lexFilePath;

  static
  {
    _lcFilePath = Chunker.class.getResource("Chunker.lc");

    if (_lcFilePath == null)
    {
      System.err.println("ERROR: Can't locate Chunker.lc in the class path.");
      System.exit(1);
    }

    _lexFilePath = Chunker.class.getResource("Chunker.lex");

    if (_lexFilePath == null)
    {
      System.err.println("ERROR: Can't locate Chunker.lex in the class path.");
      System.exit(1);
    }
  }

  private static void loadInstance()
  {
    if (instance == null)
    {
      instance = (Chunker) Learner.readLearner(_lcFilePath);
      instance.readLexiconOnDemand(_lexFilePath);
    }
  }

  public static Parser getParser() { return new edu.illinois.cs.cogcomp.lbjava.parse.ArrayFileParser("/home/sroy9/workspace/illinois-quantifier/src/main/java/edu/illinois/cs/cogcomp/quant/lbj/Chunker.ex"); }
  public static Parser getTestParser() { return null; }

  public static boolean isTraining;
  public static Chunker instance;

  public static Chunker getInstance()
  {
    loadInstance();
    return instance;
  }

  private Chunker(boolean b)
  {
    super(new Parameters());
    containingPackage = "edu.illinois.cs.cogcomp.quant.lbj";
    name = "Chunker";
    setEncoding(null);
    setLabeler(new ChunkLabel());
    setExtractor(new Chunker$$1());
    isClone = false;
  }

  public static TestingMetric getTestingMetric() { return null; }


  private boolean isClone;

  public void unclone() { isClone = false; }

  public Chunker()
  {
    super("edu.illinois.cs.cogcomp.quant.lbj.Chunker");
    isClone = true;
  }

	public Chunker(String modelPath, String lexiconPath) { this(new Parameters(), modelPath, lexiconPath); }

	public Chunker(Parameters p, String modelPath, String lexiconPath) {
		super(p);
		try {
			lcFilePath = new java.net.URL("file:" + modelPath);
			lexFilePath = new java.net.URL("file:" + lexiconPath);
		}
		catch (Exception e) {
			System.err.println("ERROR: Can't create model or lexicon URL: " + e);
			e.printStackTrace();
			System.exit(1);
		}

		if (new java.io.File(modelPath).exists()) {
			readModel(lcFilePath);
			readLexiconOnDemand(lexFilePath);
		}
		else if (IOUtilities.existsInClasspath(Chunker.class, modelPath)) {
			readModel(IOUtilities.loadFromClasspath(Chunker.class, modelPath));
			readLexiconOnDemand(IOUtilities.loadFromClasspath(Chunker.class, lexiconPath));
		}
		else {
			containingPackage = "edu.illinois.cs.cogcomp.quant.lbj";
			name = "Chunker";
			setLabeler(new ChunkLabel());
			setExtractor(new Chunker$$1());
		}

		isClone = false;
	}

  public String getInputType() { return "edu.illinois.cs.cogcomp.lbjava.nlp.seg.Token"; }
  public String getOutputType() { return "discrete"; }

  public void learn(Object example)
  {
    if (isClone)
    {
      if (!(example instanceof Token || example instanceof Object[]))
      {
        String type = example == null ? "null" : example.getClass().getName();
        System.err.println("Classifier 'Chunker(Token)' defined on line 264 of chunk.lbj received '" + type + "' as input.");
        new Exception().printStackTrace();
        System.exit(1);
      }

      loadInstance();
      instance.learn(example);
      return;
    }

    if (example instanceof Object[])
    {
      Object[] a = (Object[]) example;
      if (a[0] instanceof int[])
      {
        super.learn((int[]) a[0], (double[]) a[1], (int[]) a[2], (double[]) a[3]);
        return;
      }
    }

    super.learn(example);
  }

  public void learn(Object[] examples)
  {
    if (isClone)
    {
      if (!(examples instanceof Token[] || examples instanceof Object[][]))
      {
        String type = examples == null ? "null" : examples.getClass().getName();
        System.err.println("Classifier 'Chunker(Token)' defined on line 264 of chunk.lbj received '" + type + "' as input.");
        new Exception().printStackTrace();
        System.exit(1);
      }

      loadInstance();
      instance.learn(examples);
      return;
    }

    super.learn(examples);
  }

  private Feature cachedFeatureValue(Object __example)
  {
    Token word = (Token) __example;
    String __cachedValue = word.type;

    if (__cachedValue != null)
    {
      Feature result = new DiscretePrimitiveStringFeature(containingPackage, name, "", __cachedValue, valueIndexOf(__cachedValue), (short) allowableValues().length);
      return result;
    }

    Feature __result;
    __result = super.featureValue(__example);
    __cachedValue = word.type = __result.getStringValue();
    return __result;
  }

  public FeatureVector classify(Object __example)
  {
    if (isClone)
    {
      if (!(__example instanceof Token || __example instanceof Object[]))
      {
        String type = __example == null ? "null" : __example.getClass().getName();
        System.err.println("Classifier 'Chunker(Token)' defined on line 264 of chunk.lbj received '" + type + "' as input.");
        new Exception().printStackTrace();
        System.exit(1);
      }

      loadInstance();
      return instance.classify(__example);
    }

    if (__example instanceof Object[])
    {
      Object[] a = (Object[]) __example;
      if (a[0] instanceof int[])
        return super.classify((int[]) a[0], (double[]) a[1]);
    }

    return new FeatureVector(cachedFeatureValue(__example));
  }

  public Feature featureValue(Object __example)
  {
    if (isClone)
    {
      if (!(__example instanceof Token || __example instanceof Object[]))
      {
        String type = __example == null ? "null" : __example.getClass().getName();
        System.err.println("Classifier 'Chunker(Token)' defined on line 264 of chunk.lbj received '" + type + "' as input.");
        new Exception().printStackTrace();
        System.exit(1);
      }

      loadInstance();
      return instance.featureValue(__example);
    }

    if (__example instanceof Object[])
    {
      Object[] a = (Object[]) __example;
      if (a[0] instanceof int[])
        return super.featureValue((int[]) a[0], (double[]) a[1]);
    }

    return cachedFeatureValue(__example);
  }

  public String discreteValue(Object __example)
  {
    if (isClone)
    {
      if (!(__example instanceof Token || __example instanceof Object[]))
      {
        String type = __example == null ? "null" : __example.getClass().getName();
        System.err.println("Classifier 'Chunker(Token)' defined on line 264 of chunk.lbj received '" + type + "' as input.");
        new Exception().printStackTrace();
        System.exit(1);
      }

      loadInstance();
      return instance.discreteValue(__example);
    }

    if (__example instanceof Object[])
    {
      Object[] a = (Object[]) __example;
      if (a[0] instanceof int[])
        return super.discreteValue((int[]) a[0], (double[]) a[1]);
    }

    return cachedFeatureValue(__example).getStringValue();
  }

  public FeatureVector[] classify(Object[] examples)
  {
    if (isClone)
    {
      if (!(examples instanceof Token[] || examples instanceof Object[][]))
      {
        String type = examples == null ? "null" : examples.getClass().getName();
        System.err.println("Classifier 'Chunker(Token)' defined on line 264 of chunk.lbj received '" + type + "' as input.");
        new Exception().printStackTrace();
        System.exit(1);
      }

      loadInstance();
      return instance.classify(examples);
    }

    FeatureVector[] result = super.classify(examples);
    return result;
  }

  public static void main(String[] args)
  {
    String testParserName = null;
    String testFile = null;
    Parser testParser = getTestParser();

    try
    {
      if (!args[0].equals("null"))
        testParserName = args[0];
      if (args.length > 1) testFile = args[1];

      if (testParserName == null && testParser == null)
      {
        System.err.println("The \"testFrom\" clause was not used in the learning classifier expression that");
        System.err.println("generated this classifier, so a parser and input file must be specified.\n");
        throw new Exception();
      }
    }
    catch (Exception e)
    {
      System.err.println("usage: edu.illinois.cs.cogcomp.quant.lbj.Chunker \\");
      System.err.println("           <parser> <input file> [<null label> [<null label> ...]]\n");
      System.err.println("     * <parser> must be the fully qualified class name of a Parser, or \"null\"");
      System.err.println("       to use the default as specified by the \"testFrom\" clause.");
      System.err.println("     * <input file> is the relative or absolute path of a file, or \"null\" to");
      System.err.println("       use the parser arguments specified by the \"testFrom\" clause.  <input");
      System.err.println("       file> can also be non-\"null\" when <parser> is \"null\" (when the parser");
      System.err.println("       specified by the \"testFrom\" clause has a single string argument");
      System.err.println("       constructor) to use an alternate file.");
      System.err.println("     * A <null label> is a label (or prediction) that should not count towards");
      System.err.println("       overall precision and recall assessments.");
      System.exit(1);
    }

    if (testParserName == null && testFile != null && !testFile.equals("null"))
      testParserName = testParser.getClass().getName();
    if (testParserName != null)
      testParser = edu.illinois.cs.cogcomp.lbjava.util.ClassUtils.getParser(testParserName, new Class[]{ String.class }, new String[]{ testFile });
    Chunker classifier = new Chunker();
    TestDiscrete tester = new TestDiscrete();
    for (int i = 2; i < args.length; ++i)
      tester.addNull(args[i]);
    TestDiscrete.testDiscrete(tester, classifier, classifier.getLabeler(), testParser, true, 0);
  }

  public int hashCode() { return "Chunker".hashCode(); }
  public boolean equals(Object o) { return o instanceof Chunker; }

  public edu.illinois.cs.cogcomp.lbjava.classify.Feature valueOf(int[] a0, double[] a1, java.util.Collection a2)
  {
    if (isClone)
    {
      loadInstance();
      return instance.valueOf(a0, a1, a2);
    }

    return super.valueOf(a0, a1, a2);
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.Feature valueOf(java.lang.Object a0, java.util.Collection a1)
  {
    if (isClone)
    {
      loadInstance();
      return instance.valueOf(a0, a1);
    }

    return super.valueOf(a0, a1);
  }

  public void write(edu.illinois.cs.cogcomp.lbjava.util.ExceptionlessOutputStream a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.write(a0);
      return;
    }

    super.write(a0);
  }

  public void write(java.io.PrintStream a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.write(a0);
      return;
    }

    super.write(a0);
  }

  public void initialize(int a0, int a1)
  {
    if (isClone)
    {
      loadInstance();
      instance.initialize(a0, a1);
      return;
    }

    super.initialize(a0, a1);
  }

  public void read(edu.illinois.cs.cogcomp.lbjava.util.ExceptionlessInputStream a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.read(a0);
      return;
    }

    super.read(a0);
  }

  public void learn(int[] a0, double[] a1, int[] a2, double[] a3)
  {
    if (isClone)
    {
      loadInstance();
      instance.learn(a0, a1, a2, a3);
      return;
    }

    super.learn(a0, a1, a2, a3);
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.Feature featureValue(int[] a0, double[] a1)
  {
    if (isClone)
    {
      loadInstance();
      return instance.featureValue(a0, a1);
    }

    return super.featureValue(a0, a1);
  }

  public java.lang.String discreteValue(int[] a0, double[] a1)
  {
    if (isClone)
    {
      loadInstance();
      return instance.discreteValue(a0, a1);
    }

    return super.discreteValue(a0, a1);
  }

  public void setLTU(edu.illinois.cs.cogcomp.lbjava.learn.LinearThresholdUnit a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.setLTU(a0);
      return;
    }

    super.setLTU(a0);
  }

  public void forget()
  {
    if (isClone)
    {
      loadInstance();
      instance.forget();
      return;
    }

    super.forget();
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.ScoreSet scores(int[] a0, double[] a1)
  {
    if (isClone)
    {
      loadInstance();
      return instance.scores(a0, a1);
    }

    return super.scores(a0, a1);
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.ScoreSet scores(int[] a0, double[] a1, java.util.Collection a2)
  {
    if (isClone)
    {
      loadInstance();
      return instance.scores(a0, a1, a2);
    }

    return super.scores(a0, a1, a2);
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.ScoreSet scores(java.lang.Object a0, java.util.Collection a1)
  {
    if (isClone)
    {
      loadInstance();
      return instance.scores(a0, a1);
    }

    return super.scores(a0, a1);
  }

  public void setLabeler(edu.illinois.cs.cogcomp.lbjava.classify.Classifier a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.setLabeler(a0);
      return;
    }

    super.setLabeler(a0);
  }

  public void setExtractor(edu.illinois.cs.cogcomp.lbjava.classify.Classifier a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.setExtractor(a0);
      return;
    }

    super.setExtractor(a0);
  }

  public void setParameters(edu.illinois.cs.cogcomp.lbjava.learn.SparseNetworkLearner.Parameters a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.setParameters(a0);
      return;
    }

    super.setParameters(a0);
  }

  public edu.illinois.cs.cogcomp.lbjava.learn.Learner.Parameters getParameters()
  {
    if (isClone)
    {
      loadInstance();
      return instance.getParameters();
    }

    return super.getParameters();
  }

  public void doneLearning()
  {
    if (isClone)
    {
      loadInstance();
      instance.doneLearning();
      return;
    }

    super.doneLearning();
  }

  public void doneWithRound()
  {
    if (isClone)
    {
      loadInstance();
      instance.doneWithRound();
      return;
    }

    super.doneWithRound();
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector classify(int[] a0, double[] a1)
  {
    if (isClone)
    {
      loadInstance();
      return instance.classify(a0, a1);
    }

    return super.classify(a0, a1);
  }

  public void write(java.lang.String a0, java.lang.String a1)
  {
    if (isClone)
    {
      loadInstance();
      instance.write(a0, a1);
      return;
    }

    super.write(a0, a1);
  }

  public void save()
  {
    if (isClone)
    {
      loadInstance();
      instance.save();
      return;
    }

    super.save();
  }

  public void read(java.lang.String a0, java.lang.String a1)
  {
    if (isClone)
    {
      loadInstance();
      instance.read(a0, a1);
      return;
    }

    super.read(a0, a1);
  }

  public void learn(edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector[] a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.learn(a0);
      return;
    }

    super.learn(a0);
  }

  public void learn(edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.learn(a0);
      return;
    }

    super.learn(a0);
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.Feature featureValue(edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector a0)
  {
    if (isClone)
    {
      loadInstance();
      return instance.featureValue(a0);
    }

    return super.featureValue(a0);
  }

  public double realValue(edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector a0)
  {
    if (isClone)
    {
      loadInstance();
      return instance.realValue(a0);
    }

    return super.realValue(a0);
  }

  public double realValue(int[] a0, double[] a1)
  {
    if (isClone)
    {
      loadInstance();
      return instance.realValue(a0, a1);
    }

    return super.realValue(a0, a1);
  }

  public java.lang.String discreteValue(edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector a0)
  {
    if (isClone)
    {
      loadInstance();
      return instance.discreteValue(a0);
    }

    return super.discreteValue(a0);
  }

  public edu.illinois.cs.cogcomp.lbjava.learn.Learner emptyClone()
  {
    if (isClone)
    {
      loadInstance();
      return instance.emptyClone();
    }

    return super.emptyClone();
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.Classifier getLabeler()
  {
    if (isClone)
    {
      loadInstance();
      return instance.getLabeler();
    }

    return super.getLabeler();
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.Classifier getExtractor()
  {
    if (isClone)
    {
      loadInstance();
      return instance.getExtractor();
    }

    return super.getExtractor();
  }

  public void setLexicon(edu.illinois.cs.cogcomp.lbjava.learn.Lexicon a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.setLexicon(a0);
      return;
    }

    super.setLexicon(a0);
  }

  public void setEncoding(java.lang.String a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.setEncoding(a0);
      return;
    }

    super.setEncoding(a0);
  }

  public edu.illinois.cs.cogcomp.lbjava.learn.Lexicon getLexicon()
  {
    if (isClone)
    {
      loadInstance();
      return instance.getLexicon();
    }

    return super.getLexicon();
  }

  public edu.illinois.cs.cogcomp.lbjava.learn.Lexicon demandLexicon()
  {
    if (isClone)
    {
      loadInstance();
      return instance.demandLexicon();
    }

    return super.demandLexicon();
  }

  public void setLabelLexicon(edu.illinois.cs.cogcomp.lbjava.learn.Lexicon a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.setLabelLexicon(a0);
      return;
    }

    super.setLabelLexicon(a0);
  }

  public edu.illinois.cs.cogcomp.lbjava.learn.Lexicon getLabelLexicon()
  {
    if (isClone)
    {
      loadInstance();
      return instance.getLabelLexicon();
    }

    return super.getLabelLexicon();
  }

  public void setModelLocation(java.lang.String a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.setModelLocation(a0);
      return;
    }

    super.setModelLocation(a0);
  }

  public void setModelLocation(java.net.URL a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.setModelLocation(a0);
      return;
    }

    super.setModelLocation(a0);
  }

  public java.net.URL getModelLocation()
  {
    if (isClone)
    {
      loadInstance();
      return instance.getModelLocation();
    }

    return super.getModelLocation();
  }

  public void setLexiconLocation(java.lang.String a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.setLexiconLocation(a0);
      return;
    }

    super.setLexiconLocation(a0);
  }

  public void setLexiconLocation(java.net.URL a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.setLexiconLocation(a0);
      return;
    }

    super.setLexiconLocation(a0);
  }

  public java.net.URL getLexiconLocation()
  {
    if (isClone)
    {
      loadInstance();
      return instance.getLexiconLocation();
    }

    return super.getLexiconLocation();
  }

  public void countFeatures(edu.illinois.cs.cogcomp.lbjava.learn.Lexicon.CountPolicy a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.countFeatures(a0);
      return;
    }

    super.countFeatures(a0);
  }

  public edu.illinois.cs.cogcomp.lbjava.learn.Lexicon getLexiconDiscardCounts()
  {
    if (isClone)
    {
      loadInstance();
      return instance.getLexiconDiscardCounts();
    }

    return super.getLexiconDiscardCounts();
  }

  public void readLexicon(java.net.URL a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.readLexicon(a0);
      return;
    }

    super.readLexicon(a0);
  }

  public void readLexicon(java.lang.String a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.readLexicon(a0);
      return;
    }

    super.readLexicon(a0);
  }

  public java.lang.Object[] getExampleArray(java.lang.Object a0)
  {
    if (isClone)
    {
      loadInstance();
      return instance.getExampleArray(a0);
    }

    return super.getExampleArray(a0);
  }

  public java.lang.Object[] getExampleArray(java.lang.Object a0, boolean a1)
  {
    if (isClone)
    {
      loadInstance();
      return instance.getExampleArray(a0, a1);
    }

    return super.getExampleArray(a0, a1);
  }

  public int getPrunedLexiconSize()
  {
    if (isClone)
    {
      loadInstance();
      return instance.getPrunedLexiconSize();
    }

    return super.getPrunedLexiconSize();
  }

  public void saveModel()
  {
    if (isClone)
    {
      loadInstance();
      instance.saveModel();
      return;
    }

    super.saveModel();
  }

  public void saveLexicon()
  {
    if (isClone)
    {
      loadInstance();
      instance.saveLexicon();
      return;
    }

    super.saveLexicon();
  }

  public void writeModel(java.lang.String a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.writeModel(a0);
      return;
    }

    super.writeModel(a0);
  }

  public void writeLexicon(java.lang.String a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.writeLexicon(a0);
      return;
    }

    super.writeLexicon(a0);
  }

  public void readModel(java.net.URL a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.readModel(a0);
      return;
    }

    super.readModel(a0);
  }

  public void readModel(java.lang.String a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.readModel(a0);
      return;
    }

    super.readModel(a0);
  }

  public void readLabelLexicon(edu.illinois.cs.cogcomp.lbjava.util.ExceptionlessInputStream a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.readLabelLexicon(a0);
      return;
    }

    super.readLabelLexicon(a0);
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.ScoreSet scores(java.lang.Object a0)
  {
    if (isClone)
    {
      loadInstance();
      return instance.scores(a0);
    }

    return super.scores(a0);
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.ScoreSet scores(edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector a0)
  {
    if (isClone)
    {
      loadInstance();
      return instance.scores(a0);
    }

    return super.scores(a0);
  }

  public void setParameters(edu.illinois.cs.cogcomp.lbjava.learn.Learner.Parameters a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.setParameters(a0);
      return;
    }

    super.setParameters(a0);
  }

  public void readLexiconOnDemand(java.net.URL a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.readLexiconOnDemand(a0);
      return;
    }

    super.readLexiconOnDemand(a0);
  }

  public void readLexiconOnDemand(java.lang.String a0)
  {
    if (isClone)
    {
      loadInstance();
      instance.readLexiconOnDemand(a0);
      return;
    }

    super.readLexiconOnDemand(a0);
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector classify(edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector a0)
  {
    if (isClone)
    {
      loadInstance();
      return instance.classify(a0);
    }

    return super.classify(a0);
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector[] classify(java.lang.Object[][] a0)
  {
    if (isClone)
    {
      loadInstance();
      return instance.classify(a0);
    }

    return super.classify(a0);
  }

  public edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector[] classify(edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector[] a0)
  {
    if (isClone)
    {
      loadInstance();
      return instance.classify(a0);
    }

    return super.classify(a0);
  }

  public static class Parameters extends SparseNetworkLearner.Parameters
  {
    public Parameters()
    {
      SparseAveragedPerceptron.Parameters p = new SparseAveragedPerceptron.Parameters();
      p.learningRate = 0.1;
      p.thickness = 2;
      baseLTU = new SparseAveragedPerceptron(p);
    }
  }
}

