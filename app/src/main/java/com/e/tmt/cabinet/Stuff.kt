package com.e.tmt.cabinet

data class Stuff (var selected: Int, var id: Int, var cabinetName: String, var cellName: String,  var itemName: String, var etc: String)

data class StuffMessage(
    var result: Stuff
)

data class CabinetForm(var cabinetName: String)