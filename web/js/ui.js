/*
 * Clockwerk: Dota 2 Rune and Camp Stacking Timer.
 * Copyright (C) 2017 AR.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
var
    f_settings_dia,
    f_gen_control,
    f_settings_butt;

$(function() {
    
    /* UI CREATION */
    $("#volume").slider({
        value: 50 // Default volume.
    });

    f_gen_control = $(".gensettings").controlgroup();

    f_settings_dia = $("#settingsdialog").dialog({
        autoOpen: false,
        modal: true,
        width: 500,
        height: 470
    });

    $("#start").dialog({
        autoOpen: false,
        modal: true
    });

    $("#shortcut").dialog({
        autoOpen: false,
        modal: true
    });
    
    $("#notifymessage").dialog({
        autoOpen: false,
        modal: true
    });


    $("input").checkboxradio({
        icon: false
    });

    f_settings_butt = $(".widget input[type=submit], .widget a, .widget button").button();
})

function refreshShortcutContent() {
    document.getElementById("shortcut").innerHTML = "You are currently changing your " + ((i_changing == 1) ? "toggle" : "mute/unmute") + " key.<br /><br />The next key you press will be set as the bind!";
}

function showNotification( msg ) {
    document.getElementById("notifymessage").innerHTML = msg;
    $("#notifymessage").dialog("open");
}

function endShortcutDia() {
    $("#shortcut").dialog("close");
    i_changing = 0;
}