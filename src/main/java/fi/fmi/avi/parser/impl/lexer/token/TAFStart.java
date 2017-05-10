package fi.fmi.avi.parser.impl.lexer.token;

import static fi.fmi.avi.parser.Lexeme.Identity.TAF_START;

import fi.fmi.avi.data.AviationWeatherMessage;
import fi.fmi.avi.data.taf.TAF;
import fi.fmi.avi.parser.Lexeme;
import fi.fmi.avi.parser.ParsingHints;
import fi.fmi.avi.parser.impl.lexer.PrioritizedLexemeVisitor;
import fi.fmi.avi.parser.impl.lexer.TACReconstructorAdapter;

/**
 * Created by rinne on 10/02/17.
 */
public class TAFStart extends PrioritizedLexemeVisitor {
    public TAFStart(final Priority prio) {
        super(prio);
    }

    @Override
    public void visit(final Lexeme token, final ParsingHints hints) {
        if (token.getFirst().equals(token) && "TAF".equalsIgnoreCase(token.getTACToken())) {
            token.identify(TAF_START);
        }
    }

    public static class Reconstructor extends TACReconstructorAdapter {

        @Override
        public <T extends AviationWeatherMessage> Lexeme getAsLexeme(final T msg, Class<T> clz, final Object specifier, final ParsingHints hints) {
            if (TAF.class.isAssignableFrom(clz)) {
                return this.getLexingFactory().createLexeme("TAF", Lexeme.Identity.TAF_START);
            } else {
                return null;
            }
        }
    }

}
