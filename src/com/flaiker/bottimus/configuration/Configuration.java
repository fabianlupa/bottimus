/******************************************************************************
 * Copyright (C) 2016 Fabian Lupa                                             *
 *                                                                            *
 * This program is free software: you can redistribute it and/or modify       *
 * it under the terms of the GNU General Public License as published by       *
 * the Free Software Foundation, either version 3 of the License, or          *
 * (at your option) any later version.                                        *
 *                                                                            *
 * This program is distributed in the hope that it will be useful,            *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 * GNU General Public License for more details.                               *
 *                                                                            *
 * You should have received a copy of the GNU General Public License          *
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.      *
 ******************************************************************************/

package com.flaiker.bottimus.configuration;

/**
 * Configuration file for the application.
 * <p>
 * Options are static fields annotated with {@link Option} to be loaded from a config file and changed using reflection
 * in {@link ConfigurationBuilder}. Default values for each option are provided here.
 */
public class Configuration {
    @Option("main_channel")
    public static String MAIN_CHANNEL = "general";
    @Option("log_channel")
    public static String LOG_CHANNEL = "";
    @Option("voice_channel")
    public static String VOICE_CHANNEL = "General";
    @Option("email")
    public static String EMAIL = "email@email.email";
    @Option("password")
    public static String PASSWORD = "password!";
    @Option("greeting")
    public static String GREETING = "Guten Tag";
    @Option("temp_file")
    public static String TEMP_FILE = "tmp.mp3";
}
