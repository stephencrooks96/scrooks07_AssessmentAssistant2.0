var app = angular.module("editTest", []);

app.directive('testInit', function() {
	
});
app.directive('addAnotherCorrectPoint', function() {
	var correctPointCount = 0;
    return {
        link: function(scope, elt, attrs) {
            scope.addAnother = function() {
            	
            	correctPointCount++;
            	var correctPointHtml = '<fieldset>';
            		correctPointHtml += '<legend>Correct Point</legend>';
            		correctPointHtml += '<div class="row">';
            		correctPointHtml += '<div class="col-sm-2">';
            		correctPointHtml += '<label for="phrase' + correctPointCount + '" class="doc">Phrase:</label>';
            		correctPointHtml += '</div>';
            		correctPointHtml += '<div class="col-sm-4">';
            		correctPointHtml += '<input type="text" id="phrase' + correctPointCount + '" style="width: 350px;" class="doc" data-ng-model="correctPoint['+ correctPointCount + '].phrase">';
            		correctPointHtml += '</div>';
            		correctPointHtml += '<div class="col-sm-2">';
            		correctPointHtml += '<label for="marksWorth' + correctPointCount + '" class="doc">Marks worth:</label>';
            		correctPointHtml += '</div>';
            		correctPointHtml += '<div class="col-sm-4">';
            		correctPointHtml += '<input type="number" id="marksWorth' + correctPointCount + '" style="width: 350px;" class="doc" data-ng-model="correctPoint['+ correctPointCount + '].marksWorth">';
            		correctPointHtml += '</div>';
            		correctPointHtml += '</div>';
            		correctPointHtml += '<div class="row">';
            		correctPointHtml += '<div class="col-sm-2">';
            		correctPointHtml += '<label for="feedbackToGive' + correctPointCount + '" class="doc">Feedback to give:</label>';
            		correctPointHtml += '</div>';
            		correctPointHtml += '<div class="col-sm-4">';
            		correctPointHtml += '<textarea id="feedbackToGive' + correctPointCount + '" class="doc" style="max-width: 350px; width: 350px; min-height: 150px;" data-ng-model="correctPoint['+ correctPointCount + '].feedback"></textarea>';
            		correctPointHtml += '</div>';
            		correctPointHtml += '<div class="col-sm-2">';
            		correctPointHtml += '<label for="alternativePhrases' + correctPointCount + '" class="doc">Alternative phrases:</label>';
            		correctPointHtml += '</div>';
            		correctPointHtml += '<div class="col-sm-4">';
            		correctPointHtml += '<textarea id="alternativePhrases' + correctPointCount + '" class="doc" style="max-width: 350px; width: 350px; min-height: 150px;" placeholder="Insert each alternative inside \' \' quotation marks." data-ng-model="correctPoint['+ correctPointCount + '].alternative"></textarea>';
            		correctPointHtml += '</div>';
            		correctPointHtml += '</div>';
            		correctPointHtml += '</fieldset>';
            	
            	var element = document.getElementById('correctPointButton');
            	element.insertAdjacentHTML('beforebegin', correctPointHtml)
                
            };
        }
    }
});


app.controller('editingTest', function ($scope, $http) {
	
	$scope.testInit = function () {
		$http.get("/selectTestByTestID", testID).then(function(response) {
	        $scope.testTitle = response.testTitle;
	        $scope.startDateTime = response.startDateTime;
	        $scope.endDateTime = response.endDateTime;
	    });
	};
	
	$scope.correctPoint = [];
	$scope.addQuestion = function () {
		
		var question = $scope.question;
		var correctPoint = $scope.correctPoint;
		var questionData = {
				question : question,
				testID : testID,
				correctPoints : correctPoint
		}
		
		$http.post('/questionCreation/newQuestion/', questionData)
		  .then(
		    function(response) {
		      // success callback
		      console.log("Successful question insertion!")
		    },
		    function(response) {
		      // failure call back
		       console.log("Error while adding question.")
		    });
		
		/*
		$http.get("/selectQuestionsByTestID", $scope.testID).then(function(response) {
	        $scope.questionData = response.data.records;
	    });
	    */
    }

	
	});