/*
 Krypsis - Write web applications based on proved technologies
 like HTML, JavaScript, CSS... and access functionality of your
 smartphone in a platform independent way.
 
 Copyright (C) 2008 - 2009 krypsis.org (have.a.go@krypsis.org) 
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

#import <Foundation/Foundation.h>

/*
 The error could not be described nearly. It is not known
 what a problem we have here
 */
extern int const ERROR_UNKNOWN;

/*
 A timeout occurs. The krypsis framework didn't aswer.
 */
extern int const ERROR_TIMEOUT;

/*
 The targeted module is not supported / implemented
 */
extern int const ERROR_MODULE_NOT_SUPPORTED;

/*
 There is something wrong with the network connection.
 */
extern int const ERROR_NETWORK;

/*
 One or many of the given parameters are
 invalid and could not be proceeded.
 */
extern int const ERROR_INVALID_PARAMETER;

/*
 The executed command is not supported
 by the framework
 */
extern int const ERROR_COMMAND_NOT_SUPPORTED;

/*
 On the requested features is not supported.
 It could be the whole module or a part of it.
 */
extern int const ERROR_CAPABILITY_NOT_SUPPORTED;

/*
 The requested observer is already running.
 Only one instance of observation is possible at
 the same time
 */
extern int const ERROR_OBSERVER_STARTED;

/*
 The user canceled the operation.
 */
extern int const ERROR_CANCELED_BY_USER;

@interface KErrors :NSObject

@end
