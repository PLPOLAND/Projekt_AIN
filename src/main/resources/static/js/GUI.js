const DEAD = "#FFFFFF";
const ALIVE = "#FFFFFF";
const KL = "#0000FF";
const GL = "#FF0000";
const KL2 = "#AA00FF";
const GL2 = "#FFFF00";
const GL3 = "#00FF00";



var size = 100;
var sizeManual = 20;

var iter = 0;

$(document).ready(function () {
    $(window).resize(function(){$("#kwadraty").css("height", $("#kwadraty").css("width"))});
    $(window).resize(function(){ustaw_wymiary_komorek(size,size)})
    $("#kwadraty").css("height", $("#kwadraty").css("width"));

    
    $(window).resize(function () {
        resizeKwadratyManual();
    });
    resizeKwadratyManual();

    podziel_na_komorki(size);
    podziel_na_komorki_manual(sizeManual);
    
    clear(size);
    $("#seed_manual").prop("checked",false);
    $("#seed").hide();
});
function resizeKwadratyManual() {
    height_man = parseInt($("#manualCells").css("height"), 10);
    width_man = parseInt($("#manualCells").css("width"), 10);
    height_win = parseInt($(window).innerHeight(), 10);
    width_win = parseInt($(window).innerWidth(), 10);

    if (width_win < height_win * 1.3) {
        console.log("Mniejsza");
        if (height_man < width_man) {
            $("#kwadratyManual").css("width", 0.65 * height_man);
            $("#kwadratyManual").css("height", 0.65 * height_man);
        }
        else {
            $("#kwadratyManual").css("width", 0.65 * width_man);
            $("#kwadratyManual").css("height", 0.65 * width_man);
        }
    }else{

        if (height_man < width_man) {
            $("#kwadratyManual").css("width", 0.9 * height_man);
            $("#kwadratyManual").css("height", 0.9 * height_man);
        }
        else {
            $("#kwadratyManual").css("width", 0.9 * width_man);
            $("#kwadratyManual").css("height", 0.9 * width_man);
        }
    }
    ustaw_wymiary_komorek_manual(sizeManual, sizeManual)
}

function podziel_na_komorki(x) {
    y = x;
    $("#kwadraty").children().remove();
    for (let i = 0; i < x; i++) {
        row =$('<div class="row"></div>')
        for (let j = 0; j < y; j++) {
            id = getCell(i,j);
            tmp = $('<div id="'+id+'" class="komorka"></div>')
            $($(row)).append($(tmp));
        }
        $("#kwadraty").append(row);
    }
    ustaw_wymiary_komorek(x, y);
}

function ustaw_wymiary_komorek(x,y){
    $(".komorka").css("height", $("#kwadraty").height() /y);
    $(".komorka").css("width", $("#kwadraty").width() /x);
    // $(".komorka").css("height",( $("#kwadraty").height() /y )- 2);//z borderem
    // $(".komorka").css("width", ($("#kwadraty").width() /x)- 2);
}
function podziel_na_komorki_manual(x) {
    y = x;
    $("#kwadratyManual").children().remove();
    for (let i = 0; i < x; i++) {
        row =$('<div class="row"></div>')
        for (let j = 0; j < y; j++) {
            id = getCell_manual(i,j);
            tmp = $('<div id="'+id+'" class="komorkaManual"></div>');
            tmp.prop("algo", 0)
            $($(row)).append($(tmp));
        }
        $("#kwadratyManual").append(row);
    }
    ustaw_wymiary_komorek_manual(x, y);
}

function ustaw_wymiary_komorek_manual(x,y){
    $(".komorkaManual").css("height", $("#kwadratyManual").height() /y);
    $(".komorkaManual").css("width", $("#kwadratyManual").width() /x);
    // $(".komorka").css("height",( $("#kwadraty").height() /y )- 2);//z borderem
    // $(".komorka").css("width", ($("#kwadraty").width() /x)- 2);
}

/**
 * 
 * @param {Number} x - wspolrzedna komorki x
 * @param {Number} y - wspolrzedna komorki y
 * @param {*} kolor - kolor hex
 */
function koloruj(x,y,kolor) {
    id = getCell(x,y);
    // console.log(kolor);
    $("#"+id).css("background-color",kolor)

}
/**
 * 
 * @param {Number} x - wspolrzedna komorki x
 * @param {Number} y - wspolrzedna komorki y
 * @param {*} kolor - kolor hex
 */
function koloruj_manual(x,y,kolor) {
    id = getCell_manual(x,y);
    // console.log(kolor);
    $("#"+id).css("background-color",kolor)

}
/**
 * 
 * @param {*} element - element do pokolorwania
 * @param {*} kolor - kolor hex
 */
function kolorujEl(element,kolor) {
    element.css("background-color",kolor)
}
function kolorujEl2(element,type) {
    switch (type) {
        case 0:
            element.css("background-color", DEAD)
            break;
    
        case 1:
            element.css("background-color", GL)
            // console.log("GL");
            break;
    
        case 2:
            element.css("background-color", KL)
            break;
        
        case 3:
            element.css("background-color", GL2)
            break;
        
        case 4:
            element.css("background-color", GL3)
            break;
        
        case 5:
            element.css("background-color", KL2)
            break;
        
        default:
            break;
    }
}
function getCell(x,y){
    id = "";
    //X
    if(x < 100 && x >= 10){
        id = id + "0" + x;
    }
    else if (x < 10) {
        id = id + "00" + x;
    }
    else
        id = id + x;
    //Y
    if (y < 100 && y >= 10) {
        id = id + "0" + y;
    }
    else if (y < 10) {
        id = id + "00" + y;
    }
    else
        id = id + y;
    return id;
}
function getCell_manual(x,y){
    id = "manual";
    //X
    if(x < 100 && x >= 10){
        id = id + "0" + x;
    }
    else if (x < 10) {
        id = id + "00" + x;
    }
    else
        id = id + x;
    //Y
    if (y < 100 && y >= 10) {
        id = id + "0" + y;
    }
    else if (y < 10) {
        id = id + "00" + y;
    }
    else
        id = id + y;
    return id;
}

function randomujkolory() {
    for (let i = 0; i < size; i++) {
        for (let j = 0; j < size; j++) {
            t = Math.random();
            if (t < 0.2) {
                koloruj(i, j, KL);
                $("#" + getCell(i,j)).prop("algo", "2");
            }
            else if (t >= 0.2 && t < 0.4) {
                koloruj(i, j, GL);
                $("#" + getCell(i,j)).prop("algo", "1");
            } else {
                koloruj(i, j, DEAD);
                $("#" + getCell(i,j)).prop("algo", "0");
            }
        }
    }
}

function kolorujzDanychIter(dane,N, iteracja) {
    
    for (let i = 0; i < N; i++) {
        for (let j = 0; j < N; j++) {
            if (iteracja == 0 ) {
                kolorujEl2($("#" + getCell(i, j)), dane[iteracja][i][j]);
                $("#" + getCell(i, j)).prop("algo", dane[iteracja][i][j]);
            }
            else if (dane[iter][i][j] != dane[iteracja][i][j]) {

                kolorujEl2($("#" + getCell(i, j)), dane[iteracja][i][j]);
                $("#" + getCell(i, j)).prop("algo", dane[iteracja][i][j]);
            }
        }
    }
    iter = iteracja 
}
function kolorujzDanych(dane,N) {
    console.log("N: "+N);
    console.log("dane: " + dane);
    for (let i = 0; i < N; i++) {
        for (let j = 0; j < N; j++) {
            kolorujEl2($("#" + getCell(i, j)), dane[i][j]);
            $("#" + getCell(i, j)).prop("algo", dane[i][j]);
        }
    } 
}
function kolorujzDanychManual(dane,N) {
    console.log("N: "+N);
    console.log("dane: " + dane);
    for (let i = 0; i < N; i++) {
        for (let j = 0; j < N; j++) {
            kolorujEl2($("#" + getCell_manual(i, j)), dane[i][j]);
            $("#" + getCell_manual(i, j)).prop("algo", dane[i][j]);
        }
    } 
}
function clear(N) {
    for (let i = 0; i < N; i++) {
        for (let j = 0; j < N; j++) {
            kolorujEl2($("#" + getCell(i, j)), 0);
            $("#" + getCell(i, j)).prop("algo", 0);
        }
    }
}