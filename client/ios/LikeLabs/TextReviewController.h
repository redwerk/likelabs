#import <UIKit/UIKit.h>

@interface TextReviewController : UIViewController <UITableViewDataSource, UITableViewDelegate>

@property (retain, nonatomic) IBOutlet UITableView *socialComments;
@property (retain, nonatomic) IBOutlet UITextView *textView;

@end
