package fi.fmi.avi.parser.impl.lexer.token;

import static fi.fmi.avi.parser.Lexeme.Identity.COLOR_CODE;
import static fi.fmi.avi.parser.Lexeme.ParsedValueName.VALUE;

import java.util.regex.Matcher;

import fi.fmi.avi.data.AviationWeatherMessage;
import fi.fmi.avi.data.metar.Metar;
import fi.fmi.avi.data.metar.TrendForecast;
import fi.fmi.avi.parser.ConversionHints;
import fi.fmi.avi.parser.Lexeme;
import fi.fmi.avi.parser.impl.lexer.FactoryBasedReconstructor;
import fi.fmi.avi.parser.impl.lexer.RegexMatchingLexemeVisitor;

/**
 * Created by rinne on 10/02/17.
 */
public class ColorCode extends RegexMatchingLexemeVisitor {

    public enum ColorState {
        BLUE("BLU"),
        WHITE("WHT"),
        GREEN("GRN"),
        YELLOW1("YLO1"),
        YELLOW2("YLO2"),
        AMBER("AMB"),
        RED("RED"),
        BLACK("BLACK"),
        BLACK_BLUE("BLACKBLU"),
        BLACK_WHITE("BLACKWHT"),
        BLACK_GREEN("BLACKGRN"),
        BLACK_YELLOW1("BLACKYLO1"),
        BLACK_YELLOW2("BLACKYLO2"),
        BLACK_AMBER("BLACKAMB"),
        BLACK_RED("BLACKRED");

        private String code;

        ColorState(final String code) {
            this.code = code;
        }

        public static ColorState forCode(final String code) {
            for (ColorState w : values()) {
                if (w.code.equals(code)) {
                    return w;
                }
            }
            return null;
        }

        public String getCode() {
            return this.code;
        }

    }

    public ColorCode(final Priority prio) {
        super("^(BLU|WHT|YLO1|YLO2|AMB|RED)|(BLACK(BLU|WHT|YLO1|YLO2|AMB|RED)?)$", prio);
    }

    @Override
    public void visitIfMatched(final Lexeme token, final Matcher match, final ConversionHints hints) {
        ColorState state;
        if (match.group(2) == null) {
            state = ColorState.forCode(match.group(1));
        } else {
            state = ColorState.forCode(match.group(2));
        }
        token.identify(COLOR_CODE);
        token.setParsedValue(VALUE, state);
    }
    
    public static class Reconstructor extends FactoryBasedReconstructor {

        @Override
        public <T extends AviationWeatherMessage> Lexeme getAsLexeme(final T msg, Class<T> clz, final ConversionHints hints, final Object... specifier) {
            Lexeme retval = null;
            
            fi.fmi.avi.data.AviationCodeListUser.ColorState color = null;
            
            if (clz.isAssignableFrom(Metar.class)) {
            	Metar metar = (Metar)msg;
            	
            	TrendForecast trend = getAs(specifier, TrendForecast.class);
            	if (trend == null) {
            		color = metar.getColorState();
            	} else {
            		color = trend.getColorState();
            	}
            }
            
            if (color != null) {
            	retval = this.createLexeme(color.name(), COLOR_CODE);
            }
            
        	return retval;
        }

    }
}
