package cloud.cinder.cindercloud.block.events;

import cloud.cinder.cindercloud.block.model.Block;

public class BlockSavedEvent {

    final Block block;

    public BlockSavedEvent(final Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
