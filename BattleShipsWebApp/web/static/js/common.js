$(function () {
    var currentUser = null;

    $.ajax({
        url: BASE_URL + "/api/session",
        method: "GET",
        dataType: "json"
    }).done(function (data) {
        currentUser = data;
    }).fail(function (xhr, text, status) {

    });

    window.CommonUtils = {
        addMessage: function (text, level, dest) {
            this.clearMessages(dest);
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
        getCurrentUser: function () {
            return currentUser;
        },
        shallowEqual: function (objA, objB) {
            // fastest way to achieve it.
            return JSON.stringify(objA) === JSON.stringify(objB)
        }
    }
});