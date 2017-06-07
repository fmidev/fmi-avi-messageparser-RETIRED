package fi.fmi.avi.parser.impl.lexer.token;

import static fi.fmi.avi.parser.Lexeme.Identity.AMENDMENT;

import fi.fmi.avi.data.AviationCodeListUser;
import fi.fmi.avi.data.AviationWeatherMessage;
import fi.fmi.avi.data.taf.TAF;
import fi.fmi.avi.parser.ConversionHints;
import fi.fmi.avi.parser.Lexeme;
import fi.fmi.avi.parser.impl.lexer.FactoryBasedReconstructor;
import fi.fmi.avi.parser.impl.lexer.PrioritizedLexemeVisitor;

/**
 * Created by rinne on 10/02/17.
 */
public class Amendment extends PrioritizedLexemeVisitor {

    public Amendment(final Priority prio) {
        super(prio);
    }

    @Override
    public void visit(final Lexeme token, final ConversionHints hints) {
        if (token.getPrevious() == token.getFirst() && "AMD".equalsIgnoreCase(token.getTACToken())) {
            token.identify(AMENDMENT);
        }
    }

    public static class Reconstructor extends FactoryBasedReconstructor {

        @Override
        public <T extends AviationWeatherMessage> Lexeme getAsLexeme(final T msg, Class<T> clz, final ConversionHints hints, final Object... specifier) {
            Lexeme retval = null;
            if (TAF.class.isAssignableFrom(clz)) {
                // Note: cancellation messages are also amendments
                if (AviationCodeListUser.TAFStatus.AMENDMENT == ((TAF) msg).getStatus() ||
                    AviationCodeListUser.TAFStatus.CANCELLATION == ((TAF) msg).getStatus()) {
                    retval = this.createLexeme("AMD", AMENDMENT);
                }

            }
            return retval;
        }
    }
}
