'use strict';

/**
 * @ngdoc service
 * @name
 * @description
 */
define(
    ['angular', "lodash", "constants"],
    function (angular, _, constants) {
        angular.module("service").factory('csvService', [
            "$http", "$document", "abstractSerivce",
            function ($http, $document, abstractSerivce) {

                var api = {

                    basePath: "excelService",
                    //separatore dei singoli elementi
                    separator: ",",

                    exportExcel: function (fileName, data, header, rowFN) {
                        var options = this._prepareRequest("POST", "json2Excel");

                        var datajson = [];
                        datajson.push(header);

                        data.forEach(function (row, index) {
                            datajson.push(rowFN(row));
                        });

                        options.responseType = 'arraybuffer';
                        options.data = datajson;

                        $http(options)
                            .then(function (response) {
                                var file = new Blob([response.data], {
                                    type: 'application/vnd.ms-excel'
                                });

                                //uso la libreria FileSaver 
                                saveAs(file, fileName + ".xlsx");
                            });
                    },

                    //prende in input un array di dati e una funzione che restituisce le singole righe
                    exportExcelLocal: function (fileName, data, rowFN) {
                        var excelContent = "data:application/vnd.ms-excel,";
                        excelContent += '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>' +
                            fileName +
                            '</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>';

                        data.forEach(function (row, index) {

                            var dataArray = rowFN(row);
                            excelContent += "<tr><td>" + dataArray.join("</td><td>") + "</tr></td>";

                        });
                        excelContent += "</table></body></html>";


                        var encodedUri = encodeURI(excelContent);
                        var link = document.createElement("a");
                        link.setAttribute("href", encodedUri);
                        link.setAttribute("download", fileName + ".xls");
                        document.body.appendChild(link); // Required for FF

                        link.click();
                        //lo rimuovo dal body
                        document.body.removeChild(link);

                    },
                    //prende in input un array di dati e una funzione che restituisce le singole righe
                    exportCSV: function (fileName, data, rowFN) {
                        var csvContent = "data:text/csv;charset=utf-8,";
                        var self = this;
                        data.forEach(function (row, index) {

                            var dataArray = rowFN(row);
                            csvContent += dataArray.join(self.separator) + "\n";

                        });

                        var encodedUri = encodeURI(csvContent);
                        var link = document.createElement("a");
                        link.setAttribute("href", encodedUri);
                        link.setAttribute("download", fileName);
                        document.body.appendChild(link); // Required for FF

                        link.click();
                        //lo rimuovo dal body
                        document.body.removeChild(link);

                    },
                    //restituisce stringa vuota se la proprietà è null o undefined
                    notNull: function (prop) {
                        if (_.isUndefined(prop)) {
                            return "";
                        }
                        if (_.isNumber(prop)) {
                            return prop;
                        }
                        return prop ? prop : "";
                    }

                };

                angular.extend(api, abstractSerivce);

                return api;
            }
        ])
    });