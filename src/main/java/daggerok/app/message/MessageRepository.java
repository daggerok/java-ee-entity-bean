package daggerok.app.message;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class MessageRepository {

  @Inject
  EntityManager em;

  @Transactional
  public Message maybeSave(final Message message) {

    return findAny(message.data).stream()
                                .findFirst()
                                .orElseGet(() -> em.merge(message));
  }

  public List<Message> findAll() {
    return em.createQuery(" select m from Message m ", Message.class)
             .getResultList();
  }

  private List<Message> findAny(final String data) {
    return em.createQuery("    select m " +
                              "  from Message m " +
                              " where lower(m.data) like lower(concat('%', :data,'%')) ", Message.class)
             .setParameter("data", data)
             .getResultList();
  }
}
