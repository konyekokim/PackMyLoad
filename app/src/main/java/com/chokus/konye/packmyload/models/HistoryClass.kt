package com.chokus.konye.packmyload.models

/**
 * Created by omen on 12/04/2018.
 */
class HistoryClass{
    var pmlHistoryType : String? = null
    var pmlHistoryDate : String? = null

    constructor(pmlHistoryType : String, pmlHistoryDate : String){
        this.pmlHistoryType = pmlHistoryType
        this.pmlHistoryDate = pmlHistoryDate
    }
}