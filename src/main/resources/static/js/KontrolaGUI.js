// Uchwyty
var sliderMultiRun;
var sliderMultiNRun;
var aliveProbability;
var GLProbability;
var KLProc;

//!uchwyty


var vizData;
var playTimer;

$(document).ready(function () {
    sliderMultiRun = $(".multirunhide");
    sliderMultiNRun = $(".multirunNhide");
    aliveProbability = $("#life"); 
    GLProbability = $("#GoLLife");
    KLProc = $("#KOL_proc");


    uaktualnij_KL_value();
    $("#viz_number").prop('max', parseInt($("#iteracji").val()));
    $("#wymiar").val(size);
    $("#multirun").click(function() {
        if ($("#multirun").prop('checked')) {
            show(sliderMultiRun);
            hide(sliderMultiNRun);
        }
        else{
            hide(sliderMultiRun);
            show(sliderMultiNRun);
        }
    })
    GLProbability.change(function () {//TODO
        var gl = parseFloat(GLProbability.val(), 10);
        if (gl>= 1 ) {
            gl = 1;
            GLProbability.val(1);
        }
        else if  (gl <0) {
            gl = 0.01
            GLProbability.val(gl);
        }
        uaktualnij_KL_value();
    })
    aliveProbability.change(function () {//TOD
        var al = parseFloat(aliveProbability.val(), 10);
        if (al >= 1){ 
            ak = 1
            aliveProbability.val(1);
        }
        else if (al < 0) {
            kl = 0.01
            aliveProbability.val(0.01);
        }
    })

    $("#wymiar").keypress(function (e) {
        //if the letter is not digit then display error and don't type anything
        if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
            return false;
        }
    });
    $("#iteracji").keypress(function (e) {
        //if the letter is not digit then display error and don't type anything
        if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
            return false;
        }
    });
    $("#seed").keypress(function (e) {
        //if the letter is not digit then display error and don't type anything
        if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
            return false;
        }
    });
    $("#multirun_num").keypress(function (e) {
        //if the letter is not digit then display error and don't type anything
        if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
            return false;
        }
    });
    $("#speed").keypress(function (e) {
        //if the letter is not digit then display error and don't type anything
        if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
            return false;
        }
    });
    $("#viz_number").keypress(function (e) {
        //if the letter is not digit then display error and don't type anything
        if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
            return false;
        }
    });
    $("#wymiar").change(function (e) {
        if (parseInt($(this).val(), 10) > $(this).prop('max')){
            $(this).val($(this).prop('max'))
        }
        else if(parseInt($(this).val(), 10) < $(this).prop('min')){
            $(this.val($(this).prop('min')))
        }
    });
    $("#iteracji").change(function (e) {
        // if (parseInt($(this).val(), 10) > $(this).prop('max')) {
        //     $(this).val($(this).prop('max'))
        // }
        // else 
        if(parseInt($(this).val(), 10) < $(this).prop('min')){
            $(this).val($(this).prop('min'))
        }
        $("#viz_number").prop('max', parseInt($(this).val() - 1));
    });
    
    $("#multirun_num").change(function (e) {
        if(parseInt($(this).val(),10) > $(this).prop('max')){
            $(this).val($(this).prop('max'))
        }
        else if(parseInt($(this).val(), 10) < $(this).prop('min')){
            $(this).val($(this).prop('min'))
        }
    });
    $("#speed").change(function (e) {
        if(parseInt($(this).val(),10) > $(this).prop('max')){
            $(this).val($(this).prop('max'))
        }
        else if(parseInt($(this).val(), 10) < $(this).prop('min')){
            $(this).val($(this).prop('min'))
        }
    });
    $("#viz_number").change(function (e) {
        if(parseInt($(this).val(),10) > $(this).prop('max')){
            $(this).val($(this).prop('max'))
        }
        else if(parseInt($(this).val(), 10) < $(this).prop('min')){
            $(this).val($(this).prop('min'))
        }

        kolorujzDanych(vizData, $("#wymiar").val(), $("#viz_number").val());
    });
    $("#seed_rand").click(function(){
        $("#seed").val(Math.floor(Math.random()*1000000000));
    })
    $("#setSize").click(function(){
        n = parseInt($("#wymiar").val(),10);
        size = n;
        podziel_na_komorki(n,n);

        dane = [];
        dane[0] = [];
        for (let i = 0; i < n; i++) {
            dane[0][i] = [];
            for (let j = 0; j < n; j++) {
                dane[0][i][j] = 0;
                
            }
        }

        kolorujzDanych(dane,size,0);
    })
    $("#left").click(function(){
        $("#viz_number").val(parseInt($("#viz_number").val(),10)-1)
        if (parseInt($("#viz_number").val(), 10) > $("#viz_number").prop('max')) {
            $("#viz_number").val($("#viz_number").prop('max'))
            return;
        }
        else if (parseInt($("#viz_number").val(), 10) < $("#viz_number").prop('min')) {
            $("#viz_number").val($("#viz_number").prop('min'))
            return;
        }
        kolorujzDanych(vizData, $("#wymiar").val(), $("#viz_number").val());
    })
    $("#right").click(function(){
        $("#viz_number").val(parseInt($("#viz_number").val(),10)+1)
        if (parseInt($("#viz_number").val(), 10) > $("#viz_number").prop('max')) {
            $("#viz_number").val($("#viz_number").prop('max'))
            return;
        }
        else if (parseInt($("#viz_number").val(), 10) < $("#viz_number").prop('min')) {
            $("#viz_number").val($("#viz_number").prop('min'))
            return;
        }
        kolorujzDanych(vizData, $("#wymiar").val(), $("#viz_number").val());
    })
    $("#play").click(function () {
        playTimer = setTimeout(play, parseInt($("#speed").val()));
    })
    $("#stop").click(function(){
        clearTimeout(playTimer);
    })
    $("#sym_type").change(function() {
        // alert(); 
        if ($("#sym_type option:selected").val() == 2) {
            $(".sym_type_hide").show();
        }
        else{
            $(".sym_type_hide").hide();
        }
    })
    
    
    $(".komorka").click(function(){
        changeAlgoTypeOfCell_Viz($(this));
    });
    
    $("#start").click(function() {
        if ($("#sym_type option:selected").val() == 0){
            if ($("#manual").prop("checked")) {
                gl();
            }
            else {
                glParam(); 
            }
        }
        else if ($("#sym_type option:selected").val() == 1){
            if ($("#manual").prop("checked")) {
                kl();
            }
            else {
                klParam();
            }
        }
        else if ($("#sym_type option:selected").val() == 2){

        }
    });

    $("#savePngButt").click(function () {
        png();
    })

    $("#sym_type").change();
});
function play(){
    $("#viz_number").val(parseInt($("#viz_number").val(), 10) + 1)
    if (parseInt($("#viz_number").val(), 10) >= $("#viz_number").prop('max')) {
        $("#viz_number").val(parseInt($("#viz_number").val(), 10))
        clearTimeout(playTimer);
        return;
    }

    kolorujzDanych(vizData, $("#wymiar").val(), $("#viz_number").val());
    playTimer = setTimeout(play, parseInt($("#speed").val()));
}
function uaktualnij_KL_value(){//TODO
    var gl = parseFloat(GLProbability.val(),10);
    KLProc.html(roundTo(1 - gl,2));
}

function changeActive(disable,butt) {

    if (!butt.prop("checked")){
        disable.prop("disabled", false);
    }
    else
        disable.prop("disabled", true);

    // Enable #x
}
function show(what) {
    what.show(100);
}
function hide(what) {
    what.hide(100);
}

function roundTo(value, places) {
    var power = Math.pow(10, places);
    return Math.round(value * power) / power;
}

function changeAlgoTypeOfCell_Viz(element) {
    if (!$("#manual").prop("checked")){
        return;
    }
    tmp = parseInt(element.prop("algo"),10);
    if ($("#sym_type option:selected").val() == 0) {
        console.log("0");
        console.log(tmp);
        if (tmp == 0) {
            element.prop("algo",1);
        }
        else{
            element.prop("algo", 0);
        }
    }
    else if ($("#sym_type option:selected").val() == 1){
        console.log("1");
        console.log(tmp);
        if (tmp == 0) {
            element.prop("algo", 2);
        }
        else {
            element.prop("algo", 0);
        }
    }
    else{
        console.log("2");
        console.log(tmp);
        tmp += 1;
        if (tmp>5) {
            tmp = 0;
        }
        element.prop("algo",tmp)
    }
    tmp = parseInt(element.prop("algo"), 10);
    kolorujEl2(element,tmp);
}

function glParam() {
    $.ajax({
        url: "/api/glparam",
        type: 'get',
        data: {
            seed : $("#seed").val(),
            N: $("#wymiar").val(),
            iter : $("#iteracji").val(),
            prob: aliveProbability.val()
        },
        success: function (response) {
            msgbox = $("#msgWindow")
            msgbox.css("background-color", "#00FF00");
            msgbox.html("Odebrano dane do wizualizacji");
            msgbox.show(500);
            setTimeout(hideMsgBox,3000);
            console.log(response);
            vizData = response;
            kolorujzDanych(vizData,5,0);
        }
    }).fail(function (jqXHR, exception) {
        console.log(jqXHR.responseJSON.error);
        msgbox = $("#msgWindow");
        msgbox.css("background-color", "#FF0000");
        msgbox.text(jqXHR.responseJSON.error);
        msgbox.show(500);
        setTimeout(hideMsgBox, 5000);
    });

}
function gl(){
    console.log(readCells());
    $.ajax({
        url: "/api/gl",
        type: 'get',
        data: {
            iter: $("#iteracji").val(),
            tab: JSON.stringify(readCells())
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (response) {
            msgbox = $("#msgWindow");//uchwyt
            msgbox.css("background-color", "#00FF00");
            msgbox.html("Odebrano dane do wizualizacji");//msg
            msgbox.show(500);//show
            setTimeout(hideMsgBox,3000);//hide

            console.log(response);
            vizData = response;
            kolorujzDanych(vizData, 5, 0);
        },
    }).fail(function (jqXHR, exception) {
        console.log(jqXHR.responseJSON.error);
        msgbox = $("#msgWindow");
        msgbox.css("background-color", "#FF0000");
        msgbox.text(jqXHR.responseJSON.error);
        msgbox.show(500);
        setTimeout(hideMsgBox, 5000);
    });
}
function klParam() {
    $.ajax({
        url: "/api/klparam",
        type: 'get',
        data: {
            seed : $("#seed").val(),
            N: $("#wymiar").val(),
            iter : $("#iteracji").val(),
            prob: aliveProbability.val()
        },
        success: function (response) {
            msgbox = $("#msgWindow")
            msgbox.css("background-color", "#00FF00");
            msgbox.html("Odebrano dane do wizualizacji");
            msgbox.show(500);
            setTimeout(hideMsgBox,3000);
            console.log(response);
            vizData = response;
            kolorujzDanych(vizData,5,0);
        }
    }).fail(function (jqXHR, exception) {
        console.log(jqXHR.responseJSON.error);
        msgbox = $("#msgWindow");
        msgbox.css("background-color", "#FF0000");
        msgbox.text(jqXHR.responseJSON.error);
        msgbox.show(500);
        setTimeout(hideMsgBox, 5000);
    });

}
function kl(){
    $.ajax({
        url: "/api/kl",
        type: 'get',
        data: {
            iter: $("#iteracji").val(),
            tab: JSON.stringify(readCells())
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",

        success: function (response) {
            msgbox = $("#msgWindow")
            msgbox.css("background-color", "#00FF00");
            msgbox.html("Odebrano dane do wizualizacji");
            msgbox.show(500);
            setTimeout(hideMsgBox,3000);
            console.log(response);
            vizData = response;
            kolorujzDanych(vizData, 5, 0);
        }
    }).fail(function (jqXHR, exception) {
        console.log(jqXHR.responseJSON.error);
        msgbox = $("#msgWindow");
        msgbox.css("background-color", "#FF0000");
        msgbox.text(jqXHR.responseJSON.error);
        msgbox.show(500);
        setTimeout(hideMsgBox, 5000);
    });
}
function png(){
    $.ajax({
        url: "/api/png",
        type: 'post',
        data: JSON.stringify(readCells()),
        contentType: "application/json; charset=utf-8",
        dataType: "json",

        success: function (response) {
            msgbox = $("#msgWindow")
            msgbox.css("background-color", "#00FF00");
            msgbox.html("PNG zapisane");
            msgbox.show(500);
            setTimeout(hideMsgBox,3000);
            console.log(response);
        }
    }).fail(function (jqXHR, exception) {
        console.log(jqXHR.responseJSON.error);
        msgbox = $("#msgWindow");
        msgbox.css("background-color", "#FF0000");
        msgbox.text(jqXHR.responseJSON.error);
        msgbox.show(500);
        setTimeout(hideMsgBox, 5000);
    });
}
function hideMsgBox() {
    $("#msgWindow").hide(500);
}
/**
 * 
 */
 function readCells() {
    N = $("#wymiar").val();
    dane = [];
    for (let i = 0; i < N; i++) {
        dane[i] = [];
        for (let j = 0; j < N; j++) {
            dane[i][j] = parseInt($("#" + getCell(i, j)).prop("algo"));
        }
    }
    console.log(dane);
    return dane;
}