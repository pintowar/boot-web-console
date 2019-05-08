(function() {
  var $location = $(location);
  var $window = $(window);
  var $exampleSelector = $("#input-code-example-select");

  function hashChangeEventHandler() {
    var hash = $location.attr('hash');
    if (hash) {
      var decompressed = LZString.decompressFromEncodedURIComponent(hash.substring(1));
      if (decompressed) {
        inputEditor.getDoc().setValue(decompressed);
      }
      $exampleSelector.val("");
    } else {
      $exampleSelector.val("/console/examples/get-environment-info.groovy");
    }
    $exampleSelector.trigger("change");
  }

  var inputEditor = CodeMirror.fromTextArea(document.getElementById("input-code"), {
    mode: "text/x-groovy",
    lineNumbers: true,
    theme: "ambiance",
    matchBrackets: true,
    indentUnit: 2
  });

  var resultEditor = CodeMirror.fromTextArea(document.getElementById("result-code"), {
    mode: "application/x-json",
    lineNumbers: true,
    theme: "ambiance",
    matchBrackets: true,
    indentUnit: 2,
    readOnly: true
  });

  var remoteEval = function() {
    $.blockUI();
    $.ajax({
      url: "/console/groovy",
      type: "POST",
      data: { script: inputEditor.getValue() }
    }).done(function(data) {
      var output = (data.output || []).join('\n')

      resultEditor.getDoc().setValue([output, data.result].join('\n'));
    }).fail(function() {
      resultEditor.getDoc().setValue("Failed to send request.");
    }).always(function() {
      $.unblockUI();
    });
  };

  $("#send-button").on("click", remoteEval);
  inputEditor.setOption("extraKeys", {
     'Ctrl-Enter' : function(cm) {
       remoteEval();
     }
  });

  $exampleSelector.on("change", function() {
    selectedScript = $(this).val();
    if (selectedScript) {
      $.blockUI();
      $.ajax({
        url: selectedScript,
        mimeType: "text/x-groovy"
      }).done(function(data) {
        inputEditor.getDoc().setValue(data);
      }).fail(function() {
        resultEditor.getDoc().setValue("Failed to load example.");
      }).always(function() {
        $.unblockUI();
      });
    }
  });

  $("#permalink-button").on("click", function() {
    var href = $location.attr('href').replace(/#.*/g, '');;
    resultEditor.getDoc().setValue(href + '#' + LZString.compressToEncodedURIComponent(inputEditor.getValue()));
  });

  $window.on('hashchange', hashChangeEventHandler);
  $window.trigger('hashchange');
})();
