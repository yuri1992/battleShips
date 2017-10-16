$(function () {
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
        clearMessages:function() {
            if (dest === undefined)
                $('#js-messages').children('alert').remove();
            else
                dest.children('alert').remove();
        }
    }
});
