package cloud.cinder.cindercloud.block.rest;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/block")
public class BlockRestController {

    @Autowired
    private BlockService blockService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Block> getPagedBlocks(Pageable pageable) {
        return blockService.getBlocks(pageable).getContent();
    }

    @RequestMapping(value = "/{hash}")
    public DeferredResult<Block> getBlock(@PathVariable("hash") final String hash) {
        final DeferredResult<Block> result = new DeferredResult<>();
        blockService.getBlock(hash).subscribe(result::setResult, Throwable::printStackTrace);
        return result;
    }

}
