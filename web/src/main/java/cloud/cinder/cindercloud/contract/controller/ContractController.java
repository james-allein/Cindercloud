package cloud.cinder.cindercloud.contract.controller;

import cloud.cinder.cindercloud.abi.AbiDecoder;
import cloud.cinder.cindercloud.abi.domain.AbiContractFunction;
import cloud.cinder.cindercloud.contract.controller.command.AccessContractCommand;
import cloud.cinder.cindercloud.contract.controller.command.GenerateUICommand;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/contract")
public class ContractController {

    private AbiDecoder abiDecoder;

    public ContractController(final AbiDecoder abiDecoder) {
        this.abiDecoder = abiDecoder;
    }

    @GetMapping
    public String index(final ModelMap modelMap) {
        modelMap.putIfAbsent("accessContractCommand", new AccessContractCommand());
        return "contracts/access";
    }

    @PostMapping
    public String accessContract(@ModelAttribute("accesContractCommand") @Valid final AccessContractCommand accessContractCommand,
                                 final BindingResult bindingResult,
                                 final ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            return index(modelMap);
        } else {
            modelMap.addAttribute("abiContract", abiDecoder.decode(accessContractCommand.getAbi())
                    .getElements()
                    .stream()
                    .filter(x -> x instanceof AbiContractFunction).map(x -> (AbiContractFunction) x)
                    .filter(AbiContractFunction::isConstant)
                    .collect(Collectors.toList())
            );
            return index(modelMap);
        }
    }

    @PostMapping("/generate-ui")
    public String generateUIForElement(@RequestBody final GenerateUICommand abi,
                                       final ModelMap modelMap) {
        modelMap.put("element", abi.getElements()
                .stream()
                .filter(x -> x instanceof AbiContractFunction)
                .map(x -> (AbiContractFunction) x)
                .filter(x -> x.getName().equalsIgnoreCase(abi.getElementName()))
                .findFirst().orElse(null)
        );
        return "components/contract :: generate-ui";
    }
}
