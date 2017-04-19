package fi.fmi.avi.parser.impl.lexer.token;

import static fi.fmi.avi.parser.Lexeme.Identity.HORIZONTAL_VISIBILITY;
import static fi.fmi.avi.parser.Lexeme.ParsedValueName.DIRECTION;
import static fi.fmi.avi.parser.Lexeme.ParsedValueName.RELATIONAL_OPERATOR;
import static fi.fmi.avi.parser.Lexeme.ParsedValueName.UNIT;
import static fi.fmi.avi.parser.Lexeme.ParsedValueName.VALUE;

import java.util.regex.Matcher;

import fi.fmi.avi.parser.Lexeme;
import fi.fmi.avi.parser.Lexeme.Status;
import fi.fmi.avi.parser.ParsingHints;
import fi.fmi.avi.parser.impl.lexer.RecognizingAviMessageTokenLexer;
import fi.fmi.avi.parser.impl.lexer.RegexMatchingLexemeVisitor;

/**
 * Created by rinne on 10/02/17.
 */
public class MetricHorizontalVisibility extends RegexMatchingLexemeVisitor {
	
	public enum DirectionValue {
		NORTH("N", 0),
		SOUTH("S", 180),
		EAST("E", 90),
		WEST("W", 270),
		NORTH_EAST("NE", 45),
		NORTH_WEST("NW", 315),
		SOUTH_EAST("SE", 135),
		SOUTH_WEST("SW", 225),
        NO_DIRECTIONAL_VARIATION("NDV",-1);

        private String code;
        private int deg;

        DirectionValue(final String code, final int deg) {
            this.code = code;
            this.deg = deg;
        }

        public static DirectionValue forCode(final String code) {
            for (DirectionValue w : values()) {
                if (w.code.equals(code)) {
                    return w;
                }
            }
            return null;
        }
        
        public int inDegrees() {
        	return this.deg;
        }

    }
	
    public MetricHorizontalVisibility(final Priority prio) {
        super("^([0-9]{4})([A-Z]{1,2}|NDV)?$", prio);
    }

    @Override
    public void visitIfMatched(final Lexeme token, final Matcher match, final ParsingHints hints) {
        int visibility = Integer.parseInt(match.group(1));
        String direction = match.group(2);
        token.setParsedValue(UNIT, "m");
        
        if (visibility == 9999) {
            token.setParsedValue(VALUE, 10000);
            token.setParsedValue(RELATIONAL_OPERATOR, RecognizingAviMessageTokenLexer.RelationalOperator.MORE_THAN);
        } else if (visibility == 0) {
            token.setParsedValue(VALUE, 50);
            token.setParsedValue(RELATIONAL_OPERATOR, RecognizingAviMessageTokenLexer.RelationalOperator.LESS_THAN);
        } else {
        	 token.setParsedValue(VALUE, new Double(visibility));
        }
        if (direction != null) {
        	DirectionValue dv = DirectionValue.forCode(direction);
        	if (dv != null) {
        		token.setParsedValue(DIRECTION, dv);
        	} else {
        		token.identify(HORIZONTAL_VISIBILITY, Status.SYNTAX_ERROR, "Invalid visibility direction value '" + direction + "'");
        		return;
        	}
        }
        token.identify(HORIZONTAL_VISIBILITY);
    }
}
