package com.xxds.cjs.modules.info.models




 class UserInfo(map: Map<String,Any>?) {

     lateinit var token: String

     val name: String by map
     val userId: String by map
     val email: String by map
     val password: String by map
     val departmentId: String by map
     val firstName: String by map
     val lastName: String by map
     val officePhone: String by map
     val mobile: String by map
     val virtualRooms:Array<Map<String,Any>> by map
 }

