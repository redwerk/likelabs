/*
 * id_Facebook - in parent page
 * id_VKontakte - in parent page
 */
var redirect_url_fasebook =  window.location.protocol+ '//' + window.location.host + '/connector/facebook';
var redirect_url_vkontakte =  window.location.protocol+ '//' + window.location.host + '/connector/vkontakte';
var unlink_url = window.location.protocol+ '//' + window.location.host + '/connector/unlink';
$(document).ready(function(){
    updateLinkedAccount();
});
function linkFacebook() {
    var url = "https://www.facebook.com/dialog/oauth?client_id=" + id_Facebook + "&redirect_uri=" + redirect_url_fasebook + "&scope=email,publish_stream,manage_pages,user_photos" + "&display=popup";
    window.open(url, 'connector_popup', 'width=900,height=700,resizable=yes');
}
function linkVKontacte() {
    var url = "http://oauth.vk.com/authorize?client_id=" + id_VKontakte + "&redirect_uri=" + redirect_url_vkontakte + "&response_type=code" + "&scope=friends,notify,groups,photos,offline" + "&display=popup";
    window.open(url, 'connector_popup', 'resizable=yes');
}
function verifyResponseConnect(success, message) {
    if (!success) {
        errorDialog("Error connect", message);
    }
    updateLinkedAccount();
}
function unlink(network) {
    $.post(unlink_url, {"account":network}, function(response) {
        if (!response.success) {
            errorDialog("Error unlink", response.error);
        }
        updateLinkedAccount();
    });
}
function updateLinkedAccount() {
    $.get("/connector/linked", function(response) {
        if (!response.success) {
            errorDialog("Error unlink", response.error);
            return;
        }
        if (response.data.FACEBOOK) {
            $("#fb_Btn_Linked").show();
            $("#fb_Btn").hide();
        } else {
            $("#fb_Btn_Linked").hide();
            $("#fb_Btn").show();
        }
        if (response.data.VKONTAKTE) {
            $("#vk_Btn_Linked").show();
            $("#vk_Btn").hide();
        } else {
            $("#vk_Btn_Linked").hide();
            $("#vk_Btn").show();
        }
    })
}


