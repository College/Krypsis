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

@class KrypsisAppDelegate;
@class KRequest;

/**
 The command protocol describes the interface of the
 command pattern. For each action of the Krypsis framework
 exists a class that implements this protocol.
 The very important part of the command is the handling
 of the request and response.
 
 Each command is responsible for destroying the created response
 after dispatching it. The passed request will be destroyed after the
 execute method by the caller.
 
 If you need the request for a longer life cycle (in case of
 a listener for example) then you have to copy it and to handle
 the deallocation by yourself.
 */
@protocol KCommandProtocol <NSObject>

/**
 Executes the command and process the given reqest.
 */
- (void) executeWithDelegate:(KrypsisAppDelegate *) appDelegate andRequest:(KRequest *) request;

@end
