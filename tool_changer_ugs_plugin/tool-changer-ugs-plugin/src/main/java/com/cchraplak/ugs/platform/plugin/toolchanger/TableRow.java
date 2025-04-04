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

    public TableRow(Integer id) {
        this.id = id;
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
}
