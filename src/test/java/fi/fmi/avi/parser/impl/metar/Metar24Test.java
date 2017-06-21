package fi.fmi.avi.parser.impl.metar;

import static fi.fmi.avi.parser.Lexeme.Identity.AERODROME_DESIGNATOR;
import static fi.fmi.avi.parser.Lexeme.Identity.AIR_DEWPOINT_TEMPERATURE;
import static fi.fmi.avi.parser.Lexeme.Identity.AIR_PRESSURE_QNH;
import static fi.fmi.avi.parser.Lexeme.Identity.FORECAST_CHANGE_INDICATOR;
import static fi.fmi.avi.parser.Lexeme.Identity.HORIZONTAL_VISIBILITY;
import static fi.fmi.avi.parser.Lexeme.Identity.END_TOKEN;
import static fi.fmi.avi.parser.Lexeme.Identity.ISSUE_TIME;
import static fi.fmi.avi.parser.Lexeme.Identity.METAR_START;
import static fi.fmi.avi.parser.Lexeme.Identity.NO_SIGNIFICANT_WEATHER;
import static fi.fmi.avi.parser.Lexeme.Identity.RUNWAY_STATE;
import static fi.fmi.avi.parser.Lexeme.Identity.SURFACE_WIND;

import fi.fmi.avi.data.metar.Metar;
import fi.fmi.avi.data.metar.impl.MetarImpl;
import fi.fmi.avi.parser.ConversionSpecification;
import fi.fmi.avi.parser.Lexeme.Identity;
import fi.fmi.avi.parser.impl.AbstractAviMessageTest;

public class Metar24Test extends AbstractAviMessageTest<String, Metar> {

	@Override
	public String getJsonFilename() {
		return "metar/metar24.json";
	}
	
	@Override
	public String getMessage() {
		return
				"METAR EFTU 011350Z VRB02KT 0000 " +
				"22/12 Q1008 " +
				"R15L/410038 R64R/419838 "+
				"TEMPO TL1530 NSW=";
	}
	
	@Override
	public String getTokenizedMessagePrefix() {
		return "";
	}

	@Override
	public Identity[] getLexerTokenSequenceIdentity() {
		return new Identity[] {
				METAR_START, AERODROME_DESIGNATOR, ISSUE_TIME, SURFACE_WIND, HORIZONTAL_VISIBILITY, 
				AIR_DEWPOINT_TEMPERATURE, AIR_PRESSURE_QNH, 
				RUNWAY_STATE, RUNWAY_STATE,
				FORECAST_CHANGE_INDICATOR, FORECAST_CHANGE_INDICATOR, NO_SIGNIFICANT_WEATHER, END_TOKEN
		};
	}

    @Override
    public ConversionSpecification<String, Metar> getParserSpecification() {
        return ConversionSpecification.TAC_TO_METAR;
    }

    @Override
    public Class<? extends Metar> getTokenizerImplmentationClass() {
        return MetarImpl.class;
    }

}
