function cd_initAutocompleteDropdown(inputId, suggestionsUrl) {
    $("#" + inputId).select2({
        placeholder: "Start typing...", allowClear: true, width: "100%",
        ajax: {
            url: suggestionsUrl, dataType: "json", type: "post", delay: 250,
            data: function (params) {
                params.page = params.page || 1;
                return {
                    term: params.term,
                    page: params.page,
                    searchType: $(this).attr("search-type"),
                    filters: $(this).attr("search-filters")
                };
            },
            processResults: function (data, params) {
                return {
                    results: data.results,
                    pagination: {
                        more: (params.page * 15) < data.count
                    }
                };
            }
        }
    });
};