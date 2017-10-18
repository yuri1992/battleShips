$(function ($) {
    function Match() {
        this.$board = $('#js-ships-board');
        this.$attackBoard = $('#js-attack-board');
        this.$historyList = $('#js-history');
        this.$statistics = $('#js-statistics');

        this.isMyTurn = false;
        this.game = null;

        this.gameId = CommonUtils.getUrlParameter('matchId');
        if (this.gameId === null) {
            CommonUtils.addMessage("Error, Please Join the game again.")
            return;
        }
        CommonUtils.getCurrentUser();
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
                method: "GET",
                dataType: "json",
                beforeSend: function () {
                    self.triggerWaiting('Loading Game');
                }
            }).done(function (data, text) {
                self.game = data;
                self.render();
            }).fail(function (xhr, text, status) {
                CommonUtils.clearMessages();
                CommonUtils.addMessage("Error fetching player list, try again later", 'error');
            }).always(function () {
                self.removeWaiting();
            })
        },
        playTurn: function () {

        },
        removeWaiting: function () {
            $('.c-waiting').remove();
        },
        triggerWaiting: function (text) {
            var waitingIcon = $('<div class="c-waiting"><div class="modal-backdrop fade in"></div>\n' +
                '    <div class="c-loading">\n' +
                '        <span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>\n' +
                '<p>' + text + '</p>' +
                '</div></div>');
            $('body').prepend(waitingIcon);
        },

        renderShipsBoard: function () {
            var data = this.game.shipsBoard;
            var result = $("<table class='ship-table'></table>");
            for (var x in data.board) {
                if (x == 0)
                    continue;
                var tr = $("<tr></tr>");
                for (var y in data.board[x]) {
                    if (y == 0)
                        continue;
                    tr.append("<td class='" + data.board[x][y].toLowerCase() + "'></td>");
                }
                result.append(tr);
            }
            this.$board.append(result);
        },
        renderHistoryList: function () {

        },
        renderStatistics: function () {

        },
        renderAttackBoard: function () {
            var data = this.game.attackBoard;
            var result = $("<table class='ship-table'></table>");
            for (var x in data.board) {
                if (x == 0)
                    continue;
                var tr = $("<tr></tr>");
                for (var y in data.board[x]) {
                    if (y == 0)
                        continue;
                    tr.append("<td class='" + data.board[x][y].toLowerCase() + "'></td>");
                }
                result.append(tr);
            }
            this.$attackBoard.append(result);
        },
        render: function () {
            this.renderShipsBoard();
            this.renderAttackBoard();
        }
    };

    (new Match());
});