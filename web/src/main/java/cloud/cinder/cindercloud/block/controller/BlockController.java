package cloud.cinder.cindercloud.block.controller;

import cloud.cinder.cindercloud.block.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/blocks")
public class BlockController {

    @Autowired
    private BlockService blockService;

    @RequestMapping(value = "/{hash}")
    public DeferredResult<ModelAndView> getBlock(@PathVariable("hash") final String hash) {
        DeferredResult<ModelAndView> result = new DeferredResult<>();
        blockService.getBlock(hash).subscribe(block -> {
            ModelAndView modelAndView = new ModelAndView("blocks/block");
            modelAndView.addObject("block", block);
            result.setResult(
                    modelAndView
            );
        });
        return result;
    }
}
