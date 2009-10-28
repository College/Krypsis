package org.krypsis.blackberry.test

import org.krypsis.http.request.Request
import org.krypsis.http.request.RequestException
import org.krypsis.http.request.Request
import org.krypsis.module.ModuleContext
import org.krypsis.http.request.Request
import org.krypsis.http.request.Request
import org.krypsis.http.request.RequestException
import org.krypsis.http.request.Request

scenario "real world example", {

  given "url", {
    url = "﻿//192.168.1.51:3000/org/krypsis/device/base/getmetadata;DeviceSide=false;ConnectionSetup=delayed;Con"
  }

  when "new request is created", {
    r = new Request(url)
  }

  then "module is base", {
    r.getContext.shouldBe org.krypsis.module.ModuleContext.BASE
  }

  and "action should be getmetadata", {
    r.action.shouldBe "getmetadata"
  }

  and "param size should be 2", {
    r.getParameters.size().shouldBe 2
  }
}

scenario "try to create a krapsis url from an url string", {

  given "krypsis namespace", {
    namespace = "/org/krypsis/device/"
  }

  when "invalid urls are given", {
    urls = [
            null,
            "",
            "www.google.de",
            "http://192.168.1.1/",
            "http://192.168.1.1/org/krypsis/device",
            "$namespace",
            "${namespace}module",
            "${namespace}module/1234",
            "${namespace}1234/1234",
            "${namespace}1234/action",
            "${namespace}?",
            "${namespace}/",
            "${namespace}?key",
            "${namespace}?key=value",
    ]
  }

  then "the creation of them resuts in an exception", {
    urls.each { url ->
      ensureThrows(RequestException) {
        new Request(url)
      }
    }
  }
}

scenario "query is valid, module and action must be recognized", {

  when "valid urls are given", {
    url1 = "${namespace}base/action"
    url2 = "${namespace}base/a"
    url3 = "${namespace}base/a1212"
    url4 = "${namespace}base/a1212?k1=v1;k2=v2&k3=v3;k4=;k5;k6"
  }

  then "module and action are recognized", {
    url = new Request(url1)
    url.moduleContext.shouldBe org.krypsis.module.ModuleContext.BASE
    url.action.shouldBe "action"
    url.parameters.size().shouldBe 0

    url = new Request(url2)
    url.moduleContext.shouldBe org.krypsis.module.ModuleContext.BASE
    url.action.shouldBe "a"
    url.parameters.size().shouldBe 0

    url = new Request(url3)
    url.moduleContext.shouldBe org.krypsis.module.ModuleContext.BASE
    url.action.shouldBe "a1212"
    url.parameters.size().shouldBe 0

    url = new Request(url4)
    url.moduleContext.shouldBe org.krypsis.module.ModuleContext.BASE
    url.action.shouldBe "a1212"
    url.parameters.size().shouldBe 3
  }
}

scenario "query arguments should be parsed", {

  when "1. a simple &-query is given", {
    result = Request.parseArguments("key1=value1&key2=value2")
  }

  then "1. The parsing succeeded", {
    result.key1.shouldBe "value1"
    result.key2.shouldBe "value2"
  }

  when "2. a simple ;-query is given", {
    result = Request.parseArguments("key1=value1;key2=value2")
  }

  then "2. The parsing succeeded", {
    result.key1.shouldBe "value1"
    result.key2.shouldBe "value2"
  }

  when "3. mix them together", {
    result = Request.parseArguments("key1=value1&key2=value2&key3=value3;key4=value4;key5=value5&key6=value6")
  }

  then "3. The paesing succeeded", {
    6.times { index ->
      result."key${index + 1}".shouldBe "value${index + 1}"
    }
  }

  when "4. empty values are given", {
    result = Request.parseArguments("key1=value1&key2=;key3;key4&key5=&")
  }

  then "4. empty keys are removed and the rest is ok", {
    result.key1.shouldBe "value1"
    result.key2.shouldBe null
    result.key3.shouldBe null
    result.key4.shouldBe null
    result.key5.shouldBe null
  }
}