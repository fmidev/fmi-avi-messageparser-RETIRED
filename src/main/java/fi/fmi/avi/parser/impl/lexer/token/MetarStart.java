package fi.fmi.avi.parser.impl.lexer.token;

import static fi.fmi.avi.parser.Lexeme.Identity.METAR_START;

import fi.fmi.avi.data.AviationWeatherMessage;
import fi.fmi.avi.data.metar.Metar;
import fi.fmi.avi.parser.Lexeme;
import fi.fmi.avi.parser.ParsingHints;
import fi.fmi.avi.parser.impl.lexer.PrioritizedLexemeVisitor;
import fi.fmi.avi.parser.impl.lexer.TACReconstructorAdapter;

/**
 * Created by rinne on 10/02/17.
 */
public class MetarStart extends PrioritizedLexemeVisitor {
    public MetarStart(final Priority prio) {
        super(prio);
    }

    @Override
    public void visit(final Lexeme token, final ParsingHints hints) {
        if (token.getFirst().equals(token) && "METAR".equalsIgnoreCase(token.getTACToken())) {
            token.identify(METAR_START);
        }
    }

    public static class Reconstructor extends TACReconstructorAdapter {

        @Override
        public <T extends AviationWeatherMessage> Lexeme getAsLexeme(final T msg, Class<T> clz, final Object specifier, final ParsingHints hints) {
            if (Metar.class.isAssignableFrom(clz)) {
                return this.getLexingFactory().createLexeme("METAR", Lexeme.Identity.METAR_START);
            } else {
                return null;
            }

        }
    }

}
