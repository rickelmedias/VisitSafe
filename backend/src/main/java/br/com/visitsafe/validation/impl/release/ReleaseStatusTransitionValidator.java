package br.com.visitsafe.validation.impl.release;
import br.com.visitsafe.model.enums.ReleaseStatusEnum;
import br.com.visitsafe.model.release.Release;
import br.com.visitsafe.exception.ReleaseStatusTransitionException;
import br.com.visitsafe.validation.Validator;
import org.springframework.stereotype.Component;

import static br.com.visitsafe.model.enums.ReleaseStatusEnum.*;

@Component
public class ReleaseStatusTransitionValidator implements Validator<ReleaseStatusTransitionValidator.Transition> {

    @Override
    public void validate(Transition transition) {
        ReleaseStatusEnum current = transition.release().getStatus();
        ReleaseStatusEnum target = transition.targetStatus();

        if (target == UNAUTHORIZED && current != AUTHORIZED) {
            throw new ReleaseStatusTransitionException("Só é possível desautorizar uma liberação que já foi autorizada.");
        }

        if (current == CHECKED_OUT || current == UNAUTHORIZED) {
            throw new ReleaseStatusTransitionException("Não é possível alterar uma liberação já finalizada.");
        }

    }
    
    public record Transition(Release release, ReleaseStatusEnum targetStatus) {}
}