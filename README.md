# DispenserRobot
Adds more features to dispensers:
- Place blocks
- Break blocks
- Strip wood
- Unwax copper
- Scrape copper
- Till dirt
- Path dirt

## Usage

### Place Blocks
When a dispenser tries to dispense a block, it will place it instead.

### Break Blocks
When a dispenser is powered and tries to dispense a pickaxe, axe, shovel, hoe, or shears, it will attempt to break the block in front of it instead. While the block is breaking, the dispenser needs to remain powered. Blocks will take time to break depending on the tool and hardness of the block, respecting Efficiency enchantments. Tools will take damage, respecting Unbreaking enchantments and the Unbreakable tag.

### Strip wood, Unwax/scrape copper, Till dirt, Path dirt
When a dispenser tries to dispense an axe with short redstone pulse, it will strip wood, unwax and scrape copper in front of it.
When a dispenser tries to dispense a hoe with short redstone pulse, it will till dirt in front of it.
When a dispenser tries to dispense a shovel with short redstone pulse, it will path dirt in front of it.

## Build
```
./gradlew shadowJar
```
