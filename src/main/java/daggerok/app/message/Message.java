package daggerok.app.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
public class Message {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  Long id;

  String data;
}
