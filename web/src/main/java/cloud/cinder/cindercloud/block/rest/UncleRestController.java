package cloud.cinder.cindercloud.block.rest;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/rest/uncles")
public class UncleRestController {

    @Autowired
    private BlockService blockService;

    @RequestMapping("/{hash}")
    public HttpEntity<Block> getUncle(@PathVariable("hash") final String hash) {
        return blockService
                .getUncle(hash)
                .map(x -> new ResponseEntity(x, HttpStatus.OK))
                .orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }
}
