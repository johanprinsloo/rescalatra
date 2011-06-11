package com.example

import org.scalatra._
import org.scalatra.test.scalatest._
import org.scalatest.matchers._

class MyScalatraFilterSuite extends ScalatraFunSuite with ShouldMatchers {
  addFilter(classOf[MyScalatraFilter], "/*")

  test("GET / returns status 200") {
    get("/") { 
      status should equal (200)
    }
  }

  test("GET / main page content") {
    get("/") {
      status should equal (200)
      body should include ("hello")
    }
  }

  test("GET /hello/:name returns correctly") {
    get("/hello/Hadviga") {
      status should equal (200)
      response.body should equal ("<p>Hello, Hadviga</p>")
    }
  }
}
