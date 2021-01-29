const DEAD = "#000000";
const ALIVE = "#FFFFFF";
const KL = "#0000FF";
const GL = "#FF0000";
const KL2 = "#AA00FF";
const GL2 = "#FFFF00";
const GL3 = "#00FF00";



var size = 5;



$(document).ready(function () {
    $(window).resize(function(){$("#kwadraty").css("height", $("#kwadraty").css("width"))});
    $(window).resize(function(){ustaw_wymiary_komorek(size,size)})
    $("#kwadraty").css("height", $("#kwadraty").css("width"));
    podziel_na_komorki(size);
    // randomujkolory();
    clear(size);
});
function podziel_na_komorki(x) {
    y = x;
    $("#kwadraty").children().remove();
    for (let i = 0; i < x; i++) {
        row =$('<div class="row"></div>')
        for (let j = 0; j < y; j++) {
            id = "";
            if (i<10) {
                id = id + "0" + i;
            }
            else
            id = id + i;
            if (j<10) {
                id = id + "0" + j;
            }
            else
            id = id + j;
            tmp = $('<div id="'+id+'" class="komorka"></div>')
            $($(row)).append($(tmp));
        }
        $("#kwadraty").append(row);
    }
    ustaw_wymiary_komorek(x, y);
}

function ustaw_wymiary_komorek(x,y){
    $(".komorka").css("height",( $("#kwadraty").height() /y )- 2);
    $(".komorka").css("width", ($("#kwadraty").width() /x)- 2);
}

/**
 * 
 * @param {Number} x - wspolrzedna komorki x
 * @param {Number} y - wspolrzedna komorki y
 * @param {*} kolor - kolor hex
 */
function koloruj(x,y,kolor) {
    id = "";
    if (x < 10) {
        id = id + "0" + x;
    }
    else
        id = id + x;
    if (y < 10) {
        id = id + "0" + y;
    }
    else
        id = id + y;
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
    if (x < 10) {
        id = id + "0" + x;
    }
    else
        id = id + x;
    if (y < 10) {
        id = id + "0" + y;
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

function kolorujzDanych(dane,N, iteracja) {
    for (let i = 0; i < N; i++) {
        for (let j = 0; j < N; j++) {
            kolorujEl2($("#" + getCell(i, j)), dane[iteracja][i][j]);
            $("#" + getCell(i, j)).prop("algo", dane[iteracja][i][j]);
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