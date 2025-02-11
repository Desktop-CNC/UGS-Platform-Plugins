/*
 * Copyright (C) 2025 camren-chraplak
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cchraplak.ugs.platform.plugin.toolchanger;

/**
 *
 * @author camren-chraplak
 */
public class TableRow {
    private Integer id;
    private String name = "";
    private String description = "";
    private Integer pocket;
    private Boolean current = false;
    private Float xOff;
    private Float yOff;
    private Float zOff;
    private Float cOff;
    private Float radius;

    public TableRow(Integer id, Float xOff, Float yOff, Float zOff, Float cOff, Float radius) {
        this.id = id;
        this.xOff = xOff;
        this.yOff = yOff;
        this.zOff = zOff;
        this.cOff = cOff;
        this.radius = radius;
    }

    public TableRow(Integer id, Float xOff, Float yOff, Float zOff, Float radius) {
        this(id, xOff, yOff, zOff, null, radius);
    }

    public Integer getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPocket() {
        return this.pocket;
    }

    public void setPocket(Integer pocket) {
        this.pocket = pocket;
    }

    public Boolean getCurrent() {
        return this.current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }
    
    public Float getXOffset() {
        return this.xOff;
    }
    
    public void setXOffset(Float xOff) {
        this.xOff = xOff;
    }
    
    public Float getYOffset() {
        return this.yOff;
    }
    
    public void setYOffset(Float yOff) {
        this.yOff = yOff;
    }
    
    public Float getZOffset() {
        return this.zOff;
    }
    
    public void setZOffset(Float zOff) {
        this.zOff = zOff;
    }
    
    public Float getCOffset() {
        return this.cOff;
    }
    
    public void setCOffset(Float cOff) {
        if (this.cOff != null) {
            this.cOff = cOff;
        }
    }

    public Float getRadius() {
        return this.radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }
}
