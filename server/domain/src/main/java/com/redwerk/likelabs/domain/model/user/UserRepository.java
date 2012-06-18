package com.redwerk.likelabs.domain.model.user;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.ReviewRepository;
import com.redwerk.likelabs.domain.model.user.exception.AccountNotFoundException;
import com.redwerk.likelabs.domain.model.user.exception.DuplicatedUserException;
import com.redwerk.likelabs.domain.model.user.exception.UserNotFoundException;
import org.apache.commons.lang.Validate;

import java.util.List;

public abstract class UserRepository {
    
    public User get(long id) {
        User user = findInternal(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    protected abstract User findInternal(long id);

    public User get(SocialNetworkType snType, String accountId) {
        Validate.notNull(snType);
        Validate.notEmpty(accountId);
        User user =  findInternal(snType, accountId);
        if (user == null) {
            throw new AccountNotFoundException(snType, accountId);
        }
        return user;
    }

    protected abstract User findInternal(SocialNetworkType snType, String accountId);


    public abstract User find(String phone);


    public abstract List<User> findAll(Pager pager);

    public abstract List<User> findRegular(Pager pager);

    public abstract List<User> findClients(Point point);

    public abstract List<User> findUsersWithoutEmail();


    public abstract int getCount();

    public abstract int getRegularCount();


    public void add(User user) {
        Validate.notNull(user);
        if (find(user.getPhone()) != null) {
            throw new DuplicatedUserException(user.getPhone(), user);
        }
        addInternal(user);
    }

    protected abstract void addInternal(User user);

    public void remove(User user, ReviewRepository reviewRepository) {
        Validate.notNull(user);
        Validate.notNull(reviewRepository);
        if (canBeRemoved(user, reviewRepository)) {
            removeInternal(user);
        }
        else {
            user.archive();
        }
    }

    private boolean canBeRemoved(User user, ReviewRepository reviewRepository) {
        return (user.getStatus() == UserStatus.NOT_ACTIVATED);
    }

    protected abstract void removeInternal(User user);

}
