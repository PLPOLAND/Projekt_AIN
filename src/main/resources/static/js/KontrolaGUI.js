// Uchwyty
var sliderMultiRun;
var sliderMultiNRun;
var deadValue;
var GLValue;
var KLValue;

//!uchwyty

var playTimer;

$(document).ready(function () {
    sliderMultiRun = $(".multirunhide");
    sliderMultiNRun = $(".multirunNhide");
    deadValue = $("#dead_proc");
    GLValue = $("#GoLLife");
    KLValue = $("#KoLLife");


    uaktualnij_dead_value();
    $("#viz_number").prop('max', parseInt($("#iteracji").val()));

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
    GLValue.change(function () {
        var gl = parseFloat(GLValue.val(), 10);
        var kl = parseFloat(KLValue.val(), 10);
        if (gl+kl >= 1 ) {
            gl = 1 - kl;
            GLValue.val(roundTo(1 - kl, 2));
        }
        else if  (gl + kl <0) {
            gl = 0.01
            GLValue.val(gl);
        }
        uaktualnij_dead_value();
    })
    KLValue.change(function () {
        var gl = parseFloat(GLValue.val(), 10);
        var kl = parseFloat(KLValue.val(), 10);
        if (gl + kl >= 1){ 
            kl = 1 - kl;
            KLValue.val(roundTo(1 - gl,2));
        }
        else if (gl + kl < 0) {
            kl = 0.01
            KLValue.val(0.01);
        }
        uaktualnij_dead_value();
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
        $("#viz_number").prop('max', parseInt($(this).val()));
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

        for (let i = 0; i < size; i++) {
            for (let j = 0; j < size; j++) {
                t = Math.random();
                if (t < 0.2) {
                    koloruj(i, j, ALIVE);
                } else {
                    koloruj(i, j, DEAD);
                }
            }
        }
    });
    $("#seed_rand").change(function(){
        changeActive($("#seed"), $(this))
    })
    $("#setSize").click(function(){
        n = parseInt($("#wymiar").val(),10);
        size = n;
        podziel_na_komorki(n,n);
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
        for (let i = 0; i < size; i++) {
            for (let j = 0; j < size; j++) {
                t = Math.random();
                if (t < 0.2) {
                    koloruj(i, j, ALIVE);
                } else {
                    koloruj(i, j, DEAD);
                }
            }
        }
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
        for (let i = 0; i < size; i++) {
            for (let j = 0; j < size; j++) {
                t = Math.random();
                if (t < 0.2) {
                    koloruj(i, j, ALIVE);
                } else {
                    koloruj(i, j, DEAD);
                }
            }
        }
    })
    $("#play").click(function () {
        playTimer = setTimeout(play, parseInt($("#speed").val()));
    })
    $("#stop").click(function(){
        clearTimeout(playTimer);
    })
});
function play(){
    $("#viz_number").val(parseInt($("#viz_number").val(), 10) + 1)
    if (parseInt($("#viz_number").val(), 10) >= $("#viz_number").prop('max')) {
        $("#viz_number").val(parseInt($("#viz_number").val(), 10))
        clearTimeout(playTimer);
        return;
    }

    for (let i = 0; i < size; i++) {
        for (let j = 0; j < size; j++) {
            t = Math.random();
            if (t < 0.2) {
                koloruj(i, j, ALIVE);
            } else {
                koloruj(i, j, DEAD);
            }
        }
    }
    playTimer = setTimeout(play, parseInt($("#speed").val()));
}
function uaktualnij_dead_value(){
    var gl = parseFloat(GLValue.val(),10);
    var kl = parseFloat(KLValue.val(),10)
    deadValue.html(roundTo(1 - (gl + kl),2));
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