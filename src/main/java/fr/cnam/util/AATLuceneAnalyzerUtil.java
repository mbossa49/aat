package fr.cnam.util;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.ASCIIFoldingFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


/**
 *
 * @author ONDONGO-09929
 *
 */
public final class AATLuceneAnalyzerUtil {

	/**
	 * Liste des termes ignores � l'indexation et pour la recherche dans lucene.
	 * Elle permet d'etendre la liste de base FrenchAnalyzer.getDefaultStopSet()
	 */
	public static final String[] STOP_WORD = new String[] { "chez","-","droite","droit","gauche","drt","drte","gche","gches",
			"gch","gauches","droites","dte","bilat�ral","bilat�rale","bilater","bilat","gau"};

	/**
	 * Constructeur priv�.
	 */
	private AATLuceneAnalyzerUtil() {
		super();
	}

	public static Directory getDirectory() throws IOException{		
		FSDirectory fileDirectory = FSDirectory.open(new File("/tmp/testIndex"));
		return fileDirectory;
	}
	/**
	 *
	 * @return {@link Analyzer}
	 * @throws IOException
	 */
	public static Analyzer getAnalyzer() {

		Analyzer customAnalyzer = new Analyzer() {
			@Override
			public TokenStream tokenStream(String fieldName, Reader reader) {
				Tokenizer aatTokenizer = new WhitespaceTokenizer(Version.LUCENE_36, reader);
				TokenStream filter = new StandardFilter(Version.LUCENE_36, aatTokenizer);
				filter = new StopFilter(Version.LUCENE_36, filter, etendreFrenchStopWordSet());
				filter = new LowerCaseFilter(Version.LUCENE_36, filter);
				filter = new ASCIIFoldingFilter(filter);
				return filter;
			}
		};
		return customAnalyzer;
	}

	/**
	 *
	 * @return {@link Analyzer}
	 * @throws IOException
	 */
	public static Analyzer getSynonymeAnalyzer() {

		Analyzer customAnalyzer = new Analyzer() {			
			@Override
			public TokenStream tokenStream(String fieldName, Reader reader) {
				Tokenizer aatTokenizer = new LetterTokenizer(Version.LUCENE_36, reader);
				TokenStream filter = new StandardFilter(Version.LUCENE_36, aatTokenizer);
				filter = new StopFilter(Version.LUCENE_36, filter, etendreFrenchStopWordSet());
				filter = new LowerCaseFilter(Version.LUCENE_36, filter);
				filter = new ASCIIFoldingFilter(filter);
				return filter;
			}
		};
		return customAnalyzer;
	}

	/**
	 * Cette methode permet d'etendre la liste, par defaut, des mots fran�ais,
	 * insignifiants pour etre indexes. Ex : le, la une etc..
	 * 
	 * @return la liste resultante.
	 */
	public static Set<String> etendreFrenchStopWordSet() {

		final Set<?> frenchStopWords = FrenchAnalyzer.getDefaultStopSet();
		final Set<String> stopWordEtendu = new HashSet<String>();
		for (Object string : frenchStopWords) {
			char[] mot = (char[]) string;
			final String string2 = new String(mot);
			stopWordEtendu.add(string2);

		}

		// Extension de la liste par defaut.
		stopWordEtendu.addAll(Arrays.asList(STOP_WORD));

		// Suppression des mots une lettre.
		final Set<String> stopWordEtendu2 = new HashSet<String>();
		for (String mot : stopWordEtendu) {
			if (mot.length() > 1) {
				stopWordEtendu2.add(mot);
			}
		}
		return stopWordEtendu2;
	}
}
