package fi.fmi.avi.parser.impl.metar;

import static fi.fmi.avi.parser.Lexeme.Identity.AERODROME_DESIGNATOR;
import static fi.fmi.avi.parser.Lexeme.Identity.AIR_DEWPOINT_TEMPERATURE;
import static fi.fmi.avi.parser.Lexeme.Identity.AIR_PRESSURE_QNH;
import static fi.fmi.avi.parser.Lexeme.Identity.CLOUD;
import static fi.fmi.avi.parser.Lexeme.Identity.END_TOKEN;
import static fi.fmi.avi.parser.Lexeme.Identity.HORIZONTAL_VISIBILITY;
import static fi.fmi.avi.parser.Lexeme.Identity.ISSUE_TIME;
import static fi.fmi.avi.parser.Lexeme.Identity.METAR_START;
import static fi.fmi.avi.parser.Lexeme.Identity.SURFACE_WIND;
import static fi.fmi.avi.parser.Lexeme.Identity.WEATHER;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import fi.fmi.avi.data.metar.Metar;
import fi.fmi.avi.data.metar.impl.MetarImpl;
import fi.fmi.avi.parser.Lexeme.Identity;
import fi.fmi.avi.parser.ParserSpecification;
import fi.fmi.avi.parser.ParsingHints;
import fi.fmi.avi.parser.ParsingIssue;
import fi.fmi.avi.parser.ParsingResult.ParsingStatus;
import fi.fmi.avi.parser.TokenizingException;
import fi.fmi.avi.parser.impl.AbstractAviMessageTest;

public class Metar17Test extends AbstractAviMessageTest<String, Metar> {

	@Override
	public String getJsonFilename() {
		return "metar/metar17.json";
	}
	
	@Override
	public String getMessage() {
		return
				"METAR KORD 201004Z 05008KT 1 1/4SM -DZ BR OVC006 03/03 04/54 A2964=";
	}
	
	@Override
	public String getTokenizedMessagePrefix() {
		return "";
	}
	
	@Override
	public ParsingHints getLexerParsingHints() {
		return ParsingHints.METAR;
	}

	@Override
	public ParsingStatus getExpectedParsingStatus() {
		return ParsingStatus.WITH_ERRORS;
	}

    @Override
    public void assertParsingIssues(List<ParsingIssue> parsingIssues) {
		assertEquals(1, parsingIssues.size());
		ParsingIssue issue = parsingIssues.get(0);

        assertEquals(ParsingIssue.Type.SYNTAX_ERROR, issue.getType());
        assertEquals("More than one of AIR_DEWPOINT_TEMPERATURE in "+getMessage(), issue.getMessage());
	}

    // Remove this overridden method once the tokenizer is working
    @Override
	public void testTokenizer() throws TokenizingException, IOException {
		
	}
	
	@Override
	public Identity[] getLexerTokenSequenceIdentity() {
		return new Identity[] {
				METAR_START, AERODROME_DESIGNATOR, ISSUE_TIME, SURFACE_WIND, HORIZONTAL_VISIBILITY, WEATHER, WEATHER, CLOUD,
                AIR_DEWPOINT_TEMPERATURE, AIR_DEWPOINT_TEMPERATURE, AIR_PRESSURE_QNH, END_TOKEN
		};
	}

    @Override
    public ParserSpecification<String, Metar> getParserSpecification() {
        return ParserSpecification.TAC_TO_METAR;
    }

    @Override
    public Class<? extends Metar> getTokenizerImplmentationClass() {
        return MetarImpl.class;
    }

}
