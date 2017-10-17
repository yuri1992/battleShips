$(function ($) {
    function Match() {
        this.$board = $('#js-ships-board');
        this.$attackBoard = $('#js-attack-board');
        this.$historyList = $('#js-history');
        this.$statistics = $('#js-statistics');

        this.isMyTurn = false;
        this.game = null;

        this.init();
    }

    Match.prototype = {
        init: function () {
            this.loadGame();
        },
        loadGame: function () {
            var self = this;
            $.ajax({
                url: "/api/game/",
                method: "POST",
                dataType: "json",
                beforeSend: function () {
                    self.triggerWaiting();
                }
            }).done(function (data, text) {
                CommonUtils.addMessage("Redirecting you to the game...");
                window.location = '/pages/game';
            }).fail(function (xhr, text, status) {
                CommonUtils.clearMessages();
                CommonUtils.addMessage("Error fetching player list, try again later", 'error');
            }).always(function () {
            })
        },
        playTurn: function () {
        },

        triggerWaiting: function (text) {
            var waitingIcon = $('<div class="modal-backdrop fade in"></div>\n' +
                '    <div class="c-loading">\n' +
                '        <span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>\n' +
                '<p>' + text + '</p>' +
                '</div>');
            $('body').prepend(waitingIcon);
        },

        renderShipsBoard: function () {
        },
        renderHistoryList: function () {
        },
        renderStatistics: function () {
        },
    };

    (new Match());
});