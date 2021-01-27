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
        changeAlgoType($(this));
    });
    
    $("#start").click(function() {
       glParam(); 
    });

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

function changeAlgoType(element) {
    if (!$("#manual").prop("checked"))
        return;
    tmp = parseInt(element.prop("algo"),10);
    tmp += 1;
    if (tmp>2) {
        tmp = 0;
    }
    element.prop("algo",tmp)
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
            console.log(response);
            vizData = response;
            kolorujzDanych(vizData,5,0);
        }
    });

}