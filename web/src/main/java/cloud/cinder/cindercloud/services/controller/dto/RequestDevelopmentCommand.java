package cloud.cinder.cindercloud.services.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
public class RequestDevelopmentCommand {

    private String name;
    @NotEmpty
    private String email;
    private String company;
    @NotEmpty
    private String message;
    @NotNull
    private String title;
}
