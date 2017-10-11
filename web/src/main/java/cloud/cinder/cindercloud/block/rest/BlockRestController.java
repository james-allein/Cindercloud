package cloud.cinder.cindercloud.block.rest;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping(value = "/rest/block")
public class BlockRestController {

    @Autowired
    private BlockService blockService;

    @RequestMapping(value = "/{hash}")
    public DeferredResult<Block> getBlock(@PathVariable("hash") final String hash) {
        DeferredResult<Block> result = new DeferredResult<>();
        blockService.getBlock(hash).subscribe(block -> {
            result.setResult(block);
        }, error -> {
            error.printStackTrace();
        });
        return result;
    }

}
