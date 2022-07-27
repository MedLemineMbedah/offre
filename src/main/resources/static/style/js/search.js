function loadSearchResult()
    		{

    		 $.ajax({
    			  type: 'get',
    			  url: /*[[ @{'/url'} ]]*/,

    			  success: function(data){

    				  /*<![CDATA[*/


    				  $('.search_list').html(data);


    				  /*]]>*/
    				},

    			})

    		}