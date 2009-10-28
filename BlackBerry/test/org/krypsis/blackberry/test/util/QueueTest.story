import org.krypsis.util.Queue

scenario "am empty queue stays uneffected, if elements are requested", {

  given "empty queue with 10 response cells", {
    queue = new Queue(10);
  }

  when "call get", {
    queue.get()
  }

  then "current and last statys 0", {
    queue.current.shouldBe 0
    queue.last.shouldBe 0
  }

  and "gap should be size", {
    queue.gap.shouldBe 10
  }
}

scenario "insert element into an empty queue", {

  given "empty queue with 10 response cells", {
    queue = new Queue(10);
  }

  when "insert a new response", {
    queue.add(new Object());
  }

  then "the current is set to 0", {
    queue.current.shouldBe 0
  }

  and "the last is set to 1", {
    queue.last.shouldBe 1
  }

  and "size is 1", {
    queue.count().shouldBe 1
  }
}

scenario "fill the queue", {

  given "an empty queue", {
    size = 10
    queue = new Queue(size)
  }

  when "insert ${size} elements", {
    size.times {
      queue.add(new Object())
    }
  }

  then "the current should point to 0", {
    queue.current.shouldBe 0
  }

  and "last should point to gap", {
    queue.last.shouldBe 10
  }

  and "size should be 10", {
    queue.count().shouldBe 10
  }
}

scenario "add one element and remove it then", {

  given "an empty queue", {
    size = 10
    queue = new Queue(size)
    r = new Object()
  }

  when "add an element", {
    queue.add(r)
  }

  and "remove it then", {
    result = queue.get()
  }

  then "the result must be the added element", {
    result.shouldBe r
  }

  and "current and last must be same", {
    queue.current.shouldBe 1
    queue.current.shouldBe queue.last
  }

  and "count is null", {
    queue.count().shouldBe 0
  }
}

scenario "fill the entire queue and try to add one element", {

  given "a full queue", {
    size = 10
    queue = new Queue(size)
    size.times {
      queue.add(new Object())
    }
  }

  when "add one more element", {
    closure = { queue.add(new Object()) }
  }

  then "an exception must be thrown", {
    ensureThrows(ArrayStoreException) {
      closure()
    }
  }

  and "current must be set to 0", {
    queue.current.shouldBe 0
  }

  and "last should point the gap", {
    queue.last.shouldBe 10
    queue.last.shouldBe queue.gap
  }
}

scenario "fill the queue", {

  given "a full queue", {
    size = 10
    queue = new Queue(size)
    size.times {
      queue.add(new Object())
    }
  }

  when "remove first", {
    queue.get()
  }

  then "current is 1 and gap is 0", {
    queue.current.shouldBe 1
    queue.gap.shouldBe 0
  }

  and "last must be on the gap - 1", {
    queue.last.shouldBe 10
  }

  when "add the next element", {
    queue.add(new Object())
  }

  then "current and gap stays", {
    queue.current.shouldBe 1
    queue.gap.shouldBe 0
  }

  and "last is moved to the gap", {
    queue.last.shouldBe queue.gap
  }
}

scenario "fill the queue, empty it and access empty queue", {

  given "a full queue", {
    size = 10
    queue = new Queue(size)
    size.times {
      queue.add(new Object())
    }
  }

  when "remove 5 of the elements", {
    5.times {
      queue.get()
    }
  }

  and "add 5 again", {
    5.times {
      queue.add(new Object())
    }
  }

  then "the queue must be full", {
    queue.full.shouldBe true
  }

  and "the element size is 10", {
    queue.count().shouldBe 10
  }

  when "remove all elements", {
    10.times {
      r = queue.get()
      ensure(r) {
        isNotNull
      }
    }
  }

  then "the queue must be empty", {
    queue.empty.shouldBe true
  }

  when "accessing the elements", {
    r = queue.get()
  }

  then "element is null", {
    r.shouldBe null
  }
}


