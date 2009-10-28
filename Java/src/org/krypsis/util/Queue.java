/*
 * Krypsis - Write web applications based on proved technologies
 * like HTML, JavaScript, CSS... and access functionality of your
 * smartphone in a platform independent way.
 *
 * Copyright (C) 2008 - 2009 krypsis.org (have.a.go@krypsis.org)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package org.krypsis.util;

/**
 * Date: 08.04.2009
 *
 * This class implements a simple queue
 */
public class Queue {
  private final int size;
  private int current;
  private int last;
  private int gap;
  private Object[] objects;

  public Queue(int size) {
    this.size = size;
    clear();
  }

  /**
   * Adds an object to the queue
   *
   * @param object The object to add
   * @throws ArrayStoreException If the object could not be added. e.g. if
   * the queue is full.
   */
  public synchronized void add(Object object) throws ArrayStoreException{
    if (last == gap) {
      throw new ArrayStoreException("The queue is full!");
    }

    objects[last] = object;

    last ++;
    if (last > size) {
      last = 0;
    }
  };

  /**
   * @return The next element of the queue.
   * At the same time the element will be removed.
   */
  public synchronized Object get() {

    if (isEmpty()) {
      return null;
    }

    final Object result = last == current ? null : objects[current];
    current ++;
    gap = current - 1;

    if (current > size) {
      current = 0;
    }

    if (gap < 0) {
      gap = 0;
    }

    return result;
  }

  /**
   * Clears the queue by removing all objects
   */
  public synchronized void clear() {
    objects = new Object[size + 1];
    current = 0;
    last = 0;
    gap = size;
  }

  /**
   * @return The element count in the queue
   */
  public int count() {
    return last < current ? size + 1 + last - current : last - current;
  }

  /**
   * @return True if the queue is full. Otherwise false
   */
  public boolean isFull() {
    return count() == size;
  }

  /**
   * @return True if the queue is empty. Otherwise false.
   */
  public boolean isEmpty() {
    return count() == 0;
  }
}
