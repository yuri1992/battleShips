$(function () {
    var currentUser = null;

    $.ajax({
        url: "/api/session",
        method: "GET",
        dataType: "json"
    }).done(function (data, text) {
        currentUser = data;
    }).fail(function (xhr, text, status) {

    })

    window.CommonUtils = {
        addMessage: function (text, level, dest) {
            var messageHtml = $('<div class="alert" role="alert">' +
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
                '  <span aria-hidden="true">&times;</span>\n' +
                '</button>' + text + '</div>');

            if (level === 'error') {
                messageHtml.addClass('alert-danger');
            } else
                messageHtml.addClass('alert-success');

            if (dest === undefined)
                $('#js-messages').append(messageHtml);
            else
                dest.append(messageHtml)
        },
        clearMessages: function (dest) {
            if (dest === undefined)
                $('#js-messages').children('.alert').remove();
            else
                dest.children('.alert').remove();
        },
        getUrlParameter: function (sParam) {
            var sPageURL = decodeURIComponent(window.location.search.substring(1)),
                sURLVariables = sPageURL.split('&'),
                sParameterName,
                i;

            for (i = 0; i < sURLVariables.length; i++) {
                sParameterName = sURLVariables[i].split('=');

                if (sParameterName[0] === sParam) {
                    return sParameterName[1] === undefined ? true : sParameterName[1];
                }
            }
        },
        getCurrentUser: function () {
            return currentUser;
        },
        shallowEqual: function (objA, objB) {
            // fastest way to achieve it.
            return JSON.stringify(objA) === JSON.stringify(objB)
        }
    }
});