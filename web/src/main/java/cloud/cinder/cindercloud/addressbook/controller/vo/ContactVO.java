package cloud.cinder.cindercloud.addressbook.controller.vo;

import cloud.cinder.cindercloud.utils.domain.PrettyAmount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactVO {


    private Long id;
    private String address;
    private String nickname;
    private PrettyAmount balance;
    private String lastModified;
    private String balUsd;
    private String balEur;
}
