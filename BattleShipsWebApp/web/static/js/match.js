$(function ($) {
    function Match() {
        this.$board = $('#js-ships-board');
        this.$attackBoard = $('#js-attack-board');
        this.$historyList = $('#js-history');
        this.$statistics = $('#js-statistics');
        this.$title = $('#js-game-title');
        this.$status = $('#js-status');
        this.$gameResign = $('#js-resign');
        this.$waiting = $('.c-waiting');

        this.isMyTurn = false;
        this.game = null;
        this.previousGame = null;
        this.shouldReload = true;

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
            var self = this;
            setInterval(function () {
                if (self.shouldReload) {
                    self.loadGame();
                }
            }, 2000);
            self.loadGame();

            this.$gameResign.click(this.onGameResign.bind(this));
        },
        loadGame: function () {
            var self = this;
            $.ajax({
                url: "/api/game/",
                method: "GET",
                dataType: "json",
                beforeSend: function () {
                    self.shouldReload = false;
                }
            }).done(function (data, text) {
                if (!CommonUtils.shallowEqual(self.game, data)) {
                    // Using React based logic to rerender on object change
                    self.previousGame = self.game;
                    self.game = data;
                    self.render();
                }
            }).fail(function (xhr, text, status) {
                CommonUtils.clearMessages();
                CommonUtils.addMessage("Error fetching player list, try again later", 'error');
            }).always(function () {
                self.shouldReload = true;
            })
        },
        onCellAttack: function (row, col) {
            var data = this.game.attackBoard.board[row][col];

            if (data !== "EMPTY") {
                alert("You can't attacked this cell, you already attack it!");
                return;
            }

            var self = this;
            $.ajax({
                url: "/api/game/turn/",
                method: "POST",
                data: {
                    type: 'attack',
                    col: col,
                    row: row
                },
                dataType: "json",
                beforeSend: function () {
                    self.shouldReload = false;
                }
            }).done(function (data, text) {
                console.log(self.game.attackBoard.board[row][col], data.result);
                self.game.attackBoard.board[row][col] = data.result;
                self.renderAttackBoard();
            }).fail(function (xhr, text, status) {
                CommonUtils.clearMessages();
                CommonUtils.addMessage("Error Processing your turn, Please Try Again.", 'error');
            }).always(function () {
                self.shouldReload = true;
            })

        },
        onMineDrop: function (row, col, e) {
            console.log(row, col);
            e.preventDefault();
            var self = this;
            if (this.game.shipsBoard.minesAllowance > 0) {
                $.ajax({
                    url: "/api/game/turn/",
                    method: "POST",
                    data: {
                        type: 'mine',
                        col: col,
                        row: row
                    },
                    dataType: "json",
                    beforeSend: function () {
                        self.shouldReload = false;
                    }
                }).done(function (data, text) {
                    self.game.shipsBoard.board[row][col] = "MINE";
                    self.renderShipsBoard();

                }).fail(function (xhr, text, status) {
                    if (xhr.responseJSON)
                        alert(xhr.responseJSON.desc);
                    else {
                        CommonUtils.clearMessages();
                        CommonUtils.addMessage("Error Processing your turn, Please Try Again.", 'error');
                    }
                }).always(function () {
                    self.shouldReload = true;
                })
            }
        },
        onGameResign: function () {
            $.ajax({
                url: "/api/game/resign",
                method: "POST",
                dataType: "json",
                beforeSend: function () {
                    self.shouldReload = false;
                }
            }).done(function (data, text) {
                window.location = '/pages/matchhub';
            }).fail(function (xhr, text, status) {
                CommonUtils.clearMessages();
                CommonUtils.addMessage("Error fetching player list, try again later", 'error');
            }).always(function () {
                self.shouldReload = true;
            })
        },
        clearWaitingStatus: function () {
            this.$waiting.children().remove();
        },
        setWaitingStatus: function (text) {
            var waitingIcon = $('<div><div class="modal-backdrop fade in"></div>\n' +
                '    <div class="c-loading">\n' +
                '        <span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>\n' +
                '<p>' + text + '</p>' +
                '</div></div>');

            this.$waiting.children().remove();
            this.$waiting.append(waitingIcon);
        },

        renderShipsBoard: function () {
            var self = this;
            var data = this.game.shipsBoard;
            if (this.previousGame && CommonUtils.shallowEqual(this.previousGame.shipsBoard, data))
                return;

            this.$board.find('.ship-table').remove();

            var result = $("<table class='ship-table'></table>");
            for (var row in data.board) {
                if (row == 0)
                    continue;
                var tr = $("<tr></tr>");
                for (var col in data.board[row]) {
                    if (col == 0)
                        continue;
                    var newVar = data.board[row][col];
                    var cell = $("<td class='" + newVar.toLowerCase() + "'></td>");

                    cell.on('dragover', function (e) {
                        e.preventDefault();
                    });
                    cell.on('drop', this.onMineDrop.bind(this, row, col));

                    tr.append(cell);
                }
                result.append(tr);
            }
            this.$board.append(result);
        },
        renderMines: function () {
            var data = this.game.shipsBoard;
            if (this.previousGame && CommonUtils.shallowEqual(this.previousGame.shipsBoard, data))
                return;

            this.$board.find('.mine-wrapper').remove();
            if (data.minesAllowance > 0) {
                var result = $("<ul class='list-inline mine-wrapper'></ul>");

                for (var i = 0; i < data.minesAllowance; i++) {
                    var mine = $("<li class='icon mine' draggable=\"true\"></li>");
                    result.append(mine);
                }
                this.$board.prepend(result);
            }


        },
        renderHistoryList: function () {
            var data = this.game.turns;
            if (this.previousGame && CommonUtils.shallowEqual(this.previousGame.turns, data))
                return;

            this.$historyList.find('.turns').remove();

            var result = $("<ul class='list-group turns'></ul>");
            for (var x in data) {
                var text = "";
                var className = "";
                if (data[x].hitType === 'HIT') {
                    text = "Attack hit at " + data[x].point.x + "," + data[x].point.y + " cell successfully";
                    className = 'list-group-item-success';
                } else if (data[x].hitType === 'MISS') {
                    text = "Attack miss at " + data[x].point.x + "," + data[x].point.y + " cell";
                    className = 'list-group-item-danger';
                } else if (data[x].hitType === 'PLACE_MINE') {
                    text = "Placing mine at " + data[x].point.x + "," + data[x].point.y + " cell";
                    className = 'list-group-item-info';
                }

                result.append("<li class='list-group-item " + className + "'>" + text + "</li>");
            }

            this.$historyList.append(result);
        },
        renderStatistics: function () {
            var data = this.game.statistics;
            if (this.previousGame && CommonUtils.shallowEqual(this.previousGame.statistics, data))
                return;

            this.$statistics.find('.stats').remove();

            var result = $("<table class='table table-striped stats'></table>");
            result.append("<tr><td>Score</td><td>" + data.score + "</td></tr>");
            result.append("<tr><td>Hits</td><td>" + data.hits + "</td></tr>");
            result.append("<tr><td>Misses</td><td>" + data.misses + "</td></tr>");
            result.append("<tr><td>Turns</td><td>" + data.turns + "</td></tr>");
            result.append("<tr><td>Avg. Time</td><td>" + data.avgTurnTime + "</td></tr>");

            this.$statistics.append(result);
        },
        renderAttackBoard: function () {
            var data = this.game.attackBoard;
            if (this.previousGame && CommonUtils.shallowEqual(this.previousGame.attackBoard, data)) {
                console.log("data are the same, not rendering ", this.previousGame.attackBoard, this.game.attackboard);
                return;
            }

            this.$attackBoard.find('.ship-table').remove();

            var result = $("<table class='ship-table'></table>");
            for (var row in data.board) {
                if (row == 0)
                    continue;
                var tr = $("<tr></tr>");
                for (var col in data.board[row]) {
                    if (col == 0)
                        continue;
                    var cell = $("<td class='" + data.board[row][col].toLowerCase() + "'></td>");
                    cell.click(this.onCellAttack.bind(this, row, col));
                    tr.append(cell);
                }
                result.append(tr);
            }
            this.$attackBoard.append(result);
        },
        renderPageTitle: function () {
            this.$title.text("Playing " + this.game.matchName);
        },
        renderStatus: function () {
            if (this.game.gameStatus === "WAITING_FOR_SECOND_PLAYER_TO_JOIN") {
                this.setWaitingStatus("Waiting for second player");
            } else if (this.game.gameStatus === 'OPONENT_TURN') {
                this.setWaitingStatus("Waiting for other player to complete his turn...")
            } else if (this.game.gameStatus === 'PLAYER_TURN') {
                this.clearWaitingStatus();
            }
        },
        render: function () {
            this.renderPageTitle();
            this.renderStatus();
            this.renderShipsBoard();
            this.renderMines();
            this.renderAttackBoard();
            this.renderStatistics();
            this.renderHistoryList();
        }
    };

    (new Match());
});