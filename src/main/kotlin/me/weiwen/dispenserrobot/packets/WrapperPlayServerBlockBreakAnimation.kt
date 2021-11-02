/**
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http:></http:>//dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package me.weiwen.dispenserrobot.packets

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.BlockPosition
import org.bukkit.World
import org.bukkit.entity.Entity

class WrapperPlayServerBlockBreakAnimation : AbstractPacket {
    constructor() : super(PacketContainer(TYPE), TYPE) {
        handle.modifier.writeDefaults()
    }

    constructor(packet: PacketContainer?) : super(packet, TYPE) {}
    /**
     * Retrieve Entity ID.
     *
     *
     * Notes: entity's ID
     *
     * @return The current Entity ID
     */
    /**
     * Set Entity ID.
     *
     * @param value - new value.
     */
    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }

    /**
     * Retrieve the entity of the painting that will be spawned.
     *
     * @param world - the current world of the entity.
     * @return The spawned entity.
     */
    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    /**
     * Retrieve the entity of the painting that will be spawned.
     *
     * @param event - the packet event.
     * @return The spawned entity.
     */
    fun getEntity(event: PacketEvent): Entity {
        return getEntity(event.player.world)
    }
    /**
     * Retrieve Location.
     *
     *
     * Notes: block Position
     *
     * @return The current Location
     */
    /**
     * Set Location.
     *
     * @param value - new value.
     */
    var location: BlockPosition?
        get() = handle.blockPositionModifier.read(0)
        set(value) {
            handle.blockPositionModifier.write(0, value)
        }
    /**
     * Retrieve Destroy Stage.
     *
     *
     * Notes: 0 - 9
     *
     * @return The current Destroy Stage
     */
    /**
     * Set Destroy Stage.
     *
     * @param value - new value.
     */
    var destroyStage: Int
        get() = handle.integers.read(1)
        set(value) {
            handle.integers.write(1, value)
        }

    companion object {
        val TYPE = PacketType.Play.Server.BLOCK_BREAK_ANIMATION
    }
}