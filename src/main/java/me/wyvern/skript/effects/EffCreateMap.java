/*
 * Copyright (c) 2023. Wyvern
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.wyvern.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.ColorRGB;
import ch.njol.util.Kleenean;
import me.wyvern.SkMaps;
import me.wyvern.map.NamedMap;
import me.wyvern.map.MapManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EffCreateMap extends Effect {
    static {
        Skript.registerEffect(EffCreateMap.class, "create [a] [new] map named %string% [with [a] background [colour|color] [of] [%color%]]");
    }

    private Expression<String> map;
    private Expression<ColorRGB> color;

    @Override
    protected void execute(Event e) {
        int debugLevel = SkMaps.getDebugLevel(SkMaps.getInstance().getDebugLevel());
        if (map == null) {
            if (debugLevel >= 1) {
                Skript.warning("Map's name is null!");
            }
            return;
        }
        MapManager mapManager = SkMaps.getInstance().getMapManager();
        String mapName = map.getSingle(e);
        if (mapManager.mapExists(mapName)) {
            if (debugLevel >= 1) {
                Skript.warning("Map " + mapName + " already exists!");
            }
            return;
        }
        NamedMap namedMap = new NamedMap(mapName, map.hashCode());
        if (color != null) {
            ColorRGB colorRGB = color.getSingle(e);
            if (colorRGB != null) {
                namedMap.fill(colorRGB);
            }
        }

        mapManager.addMap(namedMap);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "create map named " + map.toString(e, debug) + " with background color " + color.toString(e, debug);
    }

    @SuppressWarnings("all")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        map = (Expression<String>) exprs[0];
        color = (Expression<ColorRGB>) exprs[1];
        return true;
    }
}
